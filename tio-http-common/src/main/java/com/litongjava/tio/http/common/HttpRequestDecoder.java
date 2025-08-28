package com.litongjava.tio.http.common;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.litongjava.constants.ServerConfigKeys;
import com.litongjava.model.sys.SysConst;
import com.litongjava.tio.core.ChannelContext;
import com.litongjava.tio.core.Node;
import com.litongjava.tio.core.Tio;
import com.litongjava.tio.core.exception.TioDecodeException;
import com.litongjava.tio.core.utils.IpBlacklistUtils;
import com.litongjava.tio.http.common.HttpConst.RequestBodyFormat;
import com.litongjava.tio.http.common.utils.HttpIpUtils;
import com.litongjava.tio.http.common.utils.HttpParseUtils;
import com.litongjava.tio.utils.environment.EnvUtils;
import com.litongjava.tio.utils.hutool.StrUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HttpRequestDecoder {

  public static enum Step {
    firstline, header, body
  }

  // 头部，最多有多少字节
  public static final int MAX_LENGTH_OF_HEADER = 20480;
  // 头部，每行最大的字节数
  public static final int MAX_LENGTH_OF_HEADERLINE = 8192;
  // 请求行的最大长度
  public static final int MAX_LENGTH_OF_REQUESTLINE = 8192;
  // 是否打印请求体
  public static boolean PRINT_PACKET = EnvUtils.getBoolean(ServerConfigKeys.SERVER_HTTP_REQUEST_PRINT_PACKET, false);

  // 性能优化：对象池化
  private static final ThreadLocal<Map<String, String>> HEADER_MAP_POOL = ThreadLocal
      .withInitial(() -> new HashMap<>(16));
  private static final ThreadLocal<byte[]> LINE_BUFFER_POOL = ThreadLocal.withInitial(() -> new byte[8192]);

  // 常用字符串常量缓存
  private static final String CLOSE = "close";
  private static final String KEEP_ALIVE = "keep-alive";
  private static final String HTTP_1_1 = "1.1";
  private static final String HTTP_1_0 = "1.0";

  /**
   * 优化后的解码方法
   */
  public static HttpRequest decode(ByteBuffer buffer, int limit, int position, int readableLength,
      ChannelContext channelContext, HttpConfig httpConfig) throws TioDecodeException {

    if (PRINT_PACKET) {
      logRequestForDebug(buffer);
    }

    // 解析请求行
    RequestLine firstLine = parseRequestLineOptimized(buffer, channelContext);
    if (firstLine == null) {
      return null;
    }

    // 复用Header Map
    Map<String, String> headers = HEADER_MAP_POOL.get();
    headers.clear();

    int contentLength = 0;
    byte[] bodyBytes = null;

    // 解析请求头
    boolean headerCompleted = parseHeaderOptimized(buffer, headers, 0, httpConfig);
    if (!headerCompleted) {
      return null;
    }

    // 处理Content-Length
    contentLength = parseContentLength(headers, channelContext, httpConfig);
    if (contentLength < 0) {
      return null; // 错误已在parseContentLength中处理
    }

    // 检查数据完整性
    int headerLength = buffer.position() - position;
    int allNeedLength = headerLength + contentLength;
    int notReceivedLength = allNeedLength - readableLength;

    if (notReceivedLength > 0) {
      optimizeBufferSize(channelContext, notReceivedLength, allNeedLength);
      return null;
    }

    // 安全检查
    String realIp = HttpIpUtils.getRealIp(channelContext, httpConfig, headers);
    if (IpBlacklistUtils.isInBlacklist(channelContext.tioConfig, realIp)) {
      throw new TioDecodeException("[" + realIp + "] in black list");
    }

    if (httpConfig.checkHost && !headers.containsKey(RequestHeaderKey.Host)) {
      throw new TioDecodeException("there is no host header");
    }

    // 创建HttpRequest
    Node realNode = createRealNode(channelContext, realIp);
    HttpRequest httpRequest = createHttpRequest(realNode, firstLine, channelContext, httpConfig, headers,
        contentLength);

    // 设置连接状态
    setConnectionStatus(httpRequest, firstLine, headers);

    // 解析查询参数
    if (StrUtil.isNotBlank(firstLine.queryString)) {
      if (!decodeParamsOptimized(httpRequest.getParams(), firstLine.queryString, httpRequest.getCharset(),
          channelContext)) {
        return null;
      }
    }

    // 处理请求体
    if (contentLength > 0) {
      bodyBytes = new byte[contentLength];
      buffer.get(bodyBytes);
      httpRequest.setBody(bodyBytes);
      parseBodyOptimized(httpRequest, firstLine, bodyBytes, channelContext, httpConfig);
    }

    return httpRequest;
  }

  /**
   * 优化后的请求行解析
   */
  public static RequestLine parseRequestLineOptimized(ByteBuffer buffer, ChannelContext channelContext)
      throws TioDecodeException {

    final int startPosition = buffer.position();
    final int maxScanLength = Math.min(buffer.remaining(), MAX_LENGTH_OF_REQUESTLINE);

    // 直接访问字节数组进行高效解析
    byte[] bytes;
    int arrayOffset;

    if (buffer.hasArray()) {
      bytes = buffer.array();
      arrayOffset = buffer.arrayOffset() + startPosition;
    } else {
      // DirectByteBuffer情况下复制到临时数组
      bytes = new byte[maxScanLength];
      int savedPosition = buffer.position();
      buffer.get(bytes, 0, maxScanLength);
      buffer.position(savedPosition);
      arrayOffset = 0;
    }

    // 使用状态机解析请求行
    int state = 0; // 0: method, 1: path, 2: query, 3: protocol, 4: version
    int[] positions = new int[10]; // 存储各部分的起始和结束位置
    int posIndex = 0;

    positions[posIndex++] = arrayOffset; // method start

    for (int i = 0; i < maxScanLength; i++) {
      byte b = bytes[arrayOffset + i];

      switch (state) {
      case 0: // parsing method
        if (b == SysConst.SPACE) {
          positions[posIndex++] = arrayOffset + i; // method end
          positions[posIndex++] = arrayOffset + i + 1; // path start
          state = 1;
        } else if (i > 10) {
          return null; // method too long
        }
        break;

      case 1: // parsing path
        if (b == SysConst.SPACE) {
          positions[posIndex++] = arrayOffset + i; // path end
          positions[posIndex++] = arrayOffset + i; // query start (empty)
          positions[posIndex++] = arrayOffset + i; // query end (empty)
          positions[posIndex++] = arrayOffset + i + 1; // protocol start
          state = 3;
        } else if (b == SysConst.ASTERISK) {
          positions[posIndex++] = arrayOffset + i; // path end
          positions[posIndex++] = arrayOffset + i + 1; // query start
          state = 2;
        }
        break;

      case 2: // parsing query
        if (b == SysConst.SPACE) {
          positions[posIndex++] = arrayOffset + i; // query end
          positions[posIndex++] = arrayOffset + i + 1; // protocol start
          state = 3;
        }
        break;

      case 3: // parsing protocol
        if (b == SysConst.BACKSLASH) {
          positions[posIndex++] = arrayOffset + i; // protocol end
          positions[posIndex++] = arrayOffset + i + 1; // version start
          state = 4;
        }
        break;

      case 4: // parsing version
        if (b == SysConst.LF) {
          int versionEnd = arrayOffset + i;
          if (i > 0 && bytes[arrayOffset + i - 1] == SysConst.CR) {
            versionEnd--;
          }
          positions[posIndex++] = versionEnd; // version end

          // 更新buffer位置
          buffer.position(startPosition + i + 1);

          return buildRequestLine(bytes, positions);
        }
        break;
      }
    }

    return null; // 未找到完整请求行
  }

  /**
   * 构建RequestLine对象
   */
  private static RequestLine buildRequestLine(byte[] bytes, int[] positions) throws TioDecodeException {
    String methodStr = new String(bytes, positions[0], positions[1] - positions[0], StandardCharsets.UTF_8);
    String pathStr = new String(bytes, positions[2], positions[3] - positions[2], StandardCharsets.UTF_8);
    String queryStr = positions[4] == positions[5] ? ""
        : new String(bytes, positions[4], positions[5] - positions[4], StandardCharsets.UTF_8);
    String protocol = new String(bytes, positions[6], positions[7] - positions[6], StandardCharsets.UTF_8);
    String version = new String(bytes, positions[8], positions[9] - positions[8], StandardCharsets.UTF_8);

    HttpMethod method = HttpMethod.from(methodStr);
    if (method == null) {
      throw new TioDecodeException("Unsupported HTTP method: " + methodStr);
    }

    RequestLine requestLine = new RequestLine();
    requestLine.setMethod(method);
    requestLine.setPath(pathStr);
    requestLine.setInitPath(pathStr);
    requestLine.setQueryString(queryStr);
    requestLine.setProtocol(protocol);
    requestLine.setVersion(version);

    return requestLine;
  }

  /**
   * 优化后的请求头解析
   */
  public static boolean parseHeaderOptimized(ByteBuffer buffer, Map<String, String> headers,
      int hasReceivedHeaderLength, HttpConfig httpConfig) throws TioDecodeException {

    byte[] lineBuffer = LINE_BUFFER_POOL.get();
    int totalHeaderLength = hasReceivedHeaderLength;

    while (buffer.hasRemaining()) {
      int lineLength = readLineBytes(buffer, lineBuffer, lineBuffer.length);

      if (lineLength == -1) {
        return false; // 需要更多数据
      }

      if (lineLength == 0) {
        return true; // 空行，header结束
      }

      if (lineLength > MAX_LENGTH_OF_HEADERLINE) {
        throw new TioDecodeException(
            "header line is too long, max length of header line is " + MAX_LENGTH_OF_HEADERLINE);
      }

      totalHeaderLength += lineLength + 2; // 加上CRLF
      if (totalHeaderLength > MAX_LENGTH_OF_HEADER) {
        throw new TioDecodeException("header is too long, max length of header is " + MAX_LENGTH_OF_HEADER);
      }

      // 高效解析header行
      int colonIndex = findByte(lineBuffer, 0, lineLength, (byte) ':');
      if (colonIndex == -1) {
        throw new TioDecodeException(
            "Invalid header line: " + new String(lineBuffer, 0, lineLength, StandardCharsets.UTF_8));
      }

      // 跳过header名称末尾的空白字符
      int nameEnd = colonIndex;
      while (nameEnd > 0 && isWhitespace(lineBuffer[nameEnd - 1])) {
        nameEnd--;
      }

      // 跳过header值开头的空白字符
      int valueStart = colonIndex + 1;
      while (valueStart < lineLength && isWhitespace(lineBuffer[valueStart])) {
        valueStart++;
      }

      // 跳过header值末尾的空白字符
      int valueEnd = lineLength;
      while (valueEnd > valueStart && isWhitespace(lineBuffer[valueEnd - 1])) {
        valueEnd--;
      }

      String name = new String(lineBuffer, 0, nameEnd, StandardCharsets.UTF_8).toLowerCase();
      String value = new String(lineBuffer, valueStart, valueEnd - valueStart, StandardCharsets.UTF_8);

      headers.put(name, value);
    }

    return false;
  }

  /**
   * 优化后的参数解析
   */
  public static boolean decodeParamsOptimized(Map<String, Object[]> params, String queryString, String charset,
      ChannelContext channelContext) throws TioDecodeException {
    if (StrUtil.isBlank(queryString)) {
      return true;
    }

    // 使用手动分割避免split()的开销
    int start = 0;
    int len = queryString.length();

    while (start < len) {
      int ampIndex = queryString.indexOf('&', start);
      int end = ampIndex == -1 ? len : ampIndex;

      if (end > start) {
        if (!parseKeyValuePair(params, queryString, start, end, charset, channelContext)) {
          return false;
        }
      }

      start = end + 1;
    }

    return true;
  }

  /**
   * 解析单个键值对
   */
  private static boolean parseKeyValuePair(Map<String, Object[]> params, String queryString, int start, int end,
      String charset, ChannelContext channelContext) throws TioDecodeException {
    int eqIndex = queryString.indexOf('=', start);

    if (eqIndex >= end || eqIndex == -1) {
      // 只有key，没有value
      String key = queryString.substring(start, end);
      addParam(params, key, null);
      return true;
    }

    // 检查是否有多个等号
    if (queryString.indexOf('=', eqIndex + 1) < end && queryString.indexOf('=', eqIndex + 1) != -1) {
      String errorMsg = "Invalid query parameter format in query string, contain multi ==:" + queryString;
      log.error(errorMsg);
      sendErrorResponse(channelContext, errorMsg);
      return false;
    }

    String key = queryString.substring(start, eqIndex);
    String value = null;

    if (eqIndex + 1 < end) {
      String encodedValue = queryString.substring(eqIndex + 1, end);
      try {
        value = URLDecoder.decode(encodedValue, charset);
      } catch (UnsupportedEncodingException e) {
        throw new TioDecodeException(e);
      }
    }

    addParam(params, key, value);
    return true;
  }

  /**
   * 添加参数到参数Map
   */
  private static void addParam(Map<String, Object[]> params, String key, String value) {
    Object[] existValue = params.get(key);
    if (existValue != null) {
      String[] newExistValue = new String[existValue.length + 1];
      System.arraycopy(existValue, 0, newExistValue, 0, existValue.length);
      newExistValue[newExistValue.length - 1] = value;
      params.put(key, newExistValue);
    } else {
      params.put(key, new String[] { value });
    }
  }

  // ========== 辅助方法 ==========

  /**
   * 读取一行字节数据到缓冲区
   */
  private static int readLineBytes(ByteBuffer buffer, byte[] lineBuffer, int maxLength) {
    int startPos = buffer.position();
    int lineLength = 0;

    while (buffer.hasRemaining() && lineLength < maxLength) {
      byte b = buffer.get();
      if (b == SysConst.CR) {
        if (buffer.hasRemaining()) {
          byte next = buffer.get();
          if (next == SysConst.LF) {
            return lineLength; // 找到完整行
          } else {
            buffer.position(buffer.position() - 1); // 回退
            lineBuffer[lineLength++] = b;
          }
        } else {
          buffer.position(startPos); // 回退到行首
          return -1; // 需要更多数据
        }
      } else {
        lineBuffer[lineLength++] = b;
      }
    }

    buffer.position(startPos); // 回退
    return -1; // 未找到完整行
  }

  /**
   * 在字节数组中查找指定字节
   */
  private static int findByte(byte[] bytes, int start, int end, byte target) {
    for (int i = start; i < end; i++) {
      if (bytes[i] == target) {
        return i;
      }
    }
    return -1;
  }

  /**
   * 检查字节是否为空白字符
   */
  private static boolean isWhitespace(byte b) {
    return b == ' ' || b == '\t' || b == '\r' || b == '\n';
  }

  /**
   * 解析Content-Length
   */
  private static int parseContentLength(Map<String, String> headers, ChannelContext channelContext,
      HttpConfig httpConfig) {
    String contentLengthStr = headers.get(RequestHeaderKey.Content_Length);

    if (StrUtil.isBlank(contentLengthStr)) {
      return 0;
    }

    try {
      int contentLength = Integer.parseInt(contentLengthStr.trim());

      if (contentLength > httpConfig.getMaxLengthOfPostBody()) {
        String message = "post body length is too big[" + contentLength + "], max length is "
            + httpConfig.getMaxLengthOfPostBody() + " byte";
        log.error(message);
        sendErrorResponse(channelContext, message, 413);
        Tio.close(channelContext, "Payload Too Large");
        return -1;
      }

      return contentLength;
    } catch (NumberFormatException e) {
      log.error("Invalid Content-Length: " + contentLengthStr);
      return 0;
    }
  }

  /**
   * 优化缓冲区大小
   */
  private static void optimizeBufferSize(ChannelContext channelContext, int notReceivedLength, int allNeedLength) {
    if (notReceivedLength > channelContext.getReadBufferSize()) {
      channelContext.setReadBufferSize(notReceivedLength);
    }
    channelContext.setPacketNeededLength(allNeedLength);
  }

  /**
   * 创建真实节点
   */
  private static Node createRealNode(ChannelContext channelContext, String realIp) {
    if (Objects.equals(realIp, channelContext.getClientNode().getIp())) {
      return channelContext.getClientNode();
    } else {
      Node realNode = new Node(realIp, channelContext.getClientNode().getPort());
      channelContext.setProxyClientNode(realNode);
      return realNode;
    }
  }

  /**
   * 创建HttpRequest对象
   */
  private static HttpRequest createHttpRequest(Node realNode, RequestLine firstLine, ChannelContext channelContext,
      HttpConfig httpConfig, Map<String, String> headers, int contentLength) {
    HttpRequest httpRequest = new HttpRequest(realNode);
    httpRequest.setRequestLine(firstLine);
    httpRequest.setChannelContext(channelContext);
    httpRequest.setHttpConfig(httpConfig);
    httpRequest.setHeaders(new HashMap<>(headers)); // 创建副本避免复用问题
    httpRequest.setContentLength(contentLength);
    return httpRequest;
  }

  /**
   * 设置连接状态
   */
  private static void setConnectionStatus(HttpRequest httpRequest, RequestLine firstLine, Map<String, String> headers) {
    String connection = headers.get(RequestHeaderKey.Connection);
    if (connection != null) {
      httpRequest.setConnection(connection.toLowerCase());
    }

    String httpVersion = firstLine.getVersion();
    boolean keepAlive = determineKeepAlive(httpVersion, connection);
    httpRequest.setKeepConnection(keepAlive);
  }

  /**
   * 确定是否保持连接
   */
  private static boolean determineKeepAlive(String httpVersion, String connection) {
    if (HTTP_1_1.equalsIgnoreCase(httpVersion)) {
      return !CLOSE.equalsIgnoreCase(connection);
    } else if (HTTP_1_0.equalsIgnoreCase(httpVersion)) {
      return KEEP_ALIVE.equalsIgnoreCase(connection);
    } else {
      return !CLOSE.equalsIgnoreCase(connection);
    }
  }

  /**
   * 发送错误响应
   */
  private static void sendErrorResponse(ChannelContext channelContext, String message) {
    sendErrorResponse(channelContext, message, 400);
  }

  private static void sendErrorResponse(ChannelContext channelContext, String message, int statusCode) {
    HttpResponse httpResponse = new HttpResponse();
    httpResponse.setStatus(statusCode);
    httpResponse.setBody(message.getBytes(StandardCharsets.UTF_8));
    Tio.bSend(channelContext, httpResponse);
    Tio.close(channelContext, message);
  }

  /**
   * 记录请求用于调试
   */
  private static void logRequestForDebug(ByteBuffer buffer) {
    buffer.mark();
    String request = StandardCharsets.UTF_8.decode(buffer).toString();
    buffer.reset();
    log.info("request:{}", request);
  }

  /**
   * 优化后的请求体解析
   */
  private static void parseBodyOptimized(HttpRequest httpRequest, RequestLine firstLine, byte[] bodyBytes,
      ChannelContext channelContext, HttpConfig httpConfig) throws TioDecodeException {
    parseBodyFormat(httpRequest, httpRequest.getHeaders());
    RequestBodyFormat bodyFormat = httpRequest.getBodyFormat();

    switch (bodyFormat) {
    case MULTIPART:
      handleMultipartBody(httpRequest, firstLine, bodyBytes, channelContext, httpConfig);
      break;

    case URLENCODED:
      handleUrlencodedBody(httpRequest, firstLine, bodyBytes, channelContext);
      break;

    default:
      handleDefaultBody(httpRequest, bodyBytes);
      break;
    }
  }

  /**
   * 处理multipart请求体
   */
  private static void handleMultipartBody(HttpRequest httpRequest, RequestLine firstLine, byte[] bodyBytes,
      ChannelContext channelContext, HttpConfig httpConfig) throws TioDecodeException {
    if (log.isInfoEnabled()) {
      logMultipartBody(httpRequest, bodyBytes, channelContext);
    }

    String contentType = httpRequest.getHeader(RequestHeaderKey.Content_Type);
    String boundary = HttpParseUtils.getSubAttribute(contentType, "boundary");

    if (log.isDebugEnabled()) {
      log.debug("{}, boundary:{}", channelContext, boundary);
    }

    HttpMultiBodyDecoder.decode(httpRequest, firstLine, bodyBytes, boundary, channelContext, httpConfig);
  }

  /**
   * 处理urlencoded请求体
   */
  private static void handleUrlencodedBody(HttpRequest httpRequest, RequestLine firstLine, byte[] bodyBytes,
      ChannelContext channelContext) throws TioDecodeException {
    try {
      String bodyString = new String(bodyBytes, httpRequest.getCharset());
      httpRequest.setBodyString(bodyString);
      decodeParamsOptimized(httpRequest.getParams(), bodyString, httpRequest.getCharset(), channelContext);
    } catch (UnsupportedEncodingException e) {
      log.error("Unsupported encoding: " + httpRequest.getCharset(), e);
      throw new TioDecodeException(e);
    }
  }

  /**
   * 处理默认请求体
   */
  private static void handleDefaultBody(HttpRequest httpRequest, byte[] bodyBytes) {
    if (bodyBytes != null && bodyBytes.length > 0) {
      try {
        String bodyString = new String(bodyBytes, httpRequest.getCharset());
        httpRequest.setBodyString(bodyString);

        if (EnvUtils.getBoolean("tio.devMode", false) && log.isInfoEnabled()) {
          log.info("body value: {}", bodyString);
        }
      } catch (UnsupportedEncodingException e) {
        log.error("Error decoding body", e);
      }
    }
  }

  /**
   * 记录multipart请求体
   */
  private static void logMultipartBody(HttpRequest httpRequest, byte[] bodyBytes, ChannelContext channelContext) {
    if (bodyBytes != null && bodyBytes.length > 0 && log.isDebugEnabled()) {
      try {
        String bodyString = new String(bodyBytes, httpRequest.getCharset());
        String logString = bodyString.length() <= 2048 ? bodyString : bodyString.substring(0, 2048);
        log.debug("{} multipart body value\r\n{}", channelContext, logString);
      } catch (UnsupportedEncodingException e) {
        log.error("Error logging multipart body", e);
      }
    }
  }

  /**
   * 解析请求体格式
   */
  public static void parseBodyFormat(HttpRequest httpRequest, Map<String, String> headers) {
    String contentType = headers.get(RequestHeaderKey.Content_Type);
    if (contentType != null) {
      contentType = contentType.toLowerCase();
    }

    if (isText(contentType)) {
      httpRequest.setBodyFormat(RequestBodyFormat.TEXT);
    } else if (contentType != null
        && contentType.startsWith(HttpConst.RequestHeaderValue.Content_Type.multipart_form_data)) {
      httpRequest.setBodyFormat(RequestBodyFormat.MULTIPART);
    } else {
      httpRequest.setBodyFormat(RequestBodyFormat.URLENCODED);
    }

    if (StrUtil.isNotBlank(contentType)) {
      String charset = HttpParseUtils.getSubAttribute(contentType, "charset");
      if (StrUtil.isNotBlank(charset)) {
        httpRequest.setCharset(charset);
      } else {
        httpRequest.setCharset(SysConst.DEFAULT_ENCODING);
      }
    }
  }

  /**
   * 检查是否为文本类型
   */
  private static boolean isText(String contentType) {
    if (contentType == null) {
      return false;
    }

    return contentType.startsWith(HttpConst.RequestHeaderValue.Content_Type.text_plain)
        || contentType.startsWith(MimeType.APPLICATION_JSON.getType());
  }
}