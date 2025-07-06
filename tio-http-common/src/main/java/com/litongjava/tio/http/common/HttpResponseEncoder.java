package com.litongjava.tio.http.common;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.litongjava.constants.ServerConfigKeys;
import com.litongjava.model.sys.SysConst;
import com.litongjava.tio.core.ChannelContext;
import com.litongjava.tio.core.TioConfig;
import com.litongjava.tio.http.common.utils.HttpDateTimer;
import com.litongjava.tio.http.common.utils.HttpGzipUtils;
import com.litongjava.tio.utils.environment.EnvUtils;
import com.litongjava.tio.utils.hutool.StrUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * HttpResponseEncoder  
 * @author tanyaowu
 * 2017年8月4日 上午9:41:12
 */
@Slf4j
public class HttpResponseEncoder {
  public static final int MAX_HEADER_LENGTH = 20480;
  public static final int HEADER_SERVER_LENGTH = HeaderName.Server.bytes.length + HeaderValue.Server.TIO.bytes.length + 3;
  public static final int HEADER_DATE_LENGTH_1 = HeaderName.Date.bytes.length + 3;
  public static final int HEADER_FIXED_LENGTH = HEADER_SERVER_LENGTH + HEADER_DATE_LENGTH_1;
  private static boolean showServer = EnvUtils.getBoolean(ServerConfigKeys.SERVER_HTTP_RESPONSE_HEANDER_SHOW_SERVER, true);

  /**
   *
   * @param httpResponse
   * @param tioConfig
   * @param channelContext
   * @return
   */
  public static ByteBuffer encode(HttpResponse httpResponse, TioConfig tioConfig, ChannelContext channelContext) {
    File fileBody = httpResponse.getFileBody();
    if (fileBody != null) {
      long length = fileBody.length();
      return buildHeader(httpResponse, length);
    }
    int bodyLength = 0;
    byte[] body = httpResponse.body;

    byte[] jsonpBytes = null;
    HttpRequest httpRequest = httpResponse.getHttpRequest();
    if (httpRequest != null) {
      String jsonp = httpRequest.getParam(httpRequest.httpConfig.getJsonpParamName());
      if (StrUtil.isNotBlank(jsonp)) {
        try {
          jsonpBytes = jsonp.getBytes(httpRequest.getCharset());
        } catch (UnsupportedEncodingException e) {
          throw new RuntimeException(e);
        }
        if (body == null) {
          body = SysConst.NULL;
        }
        byte[] bodyBs = new byte[jsonpBytes.length + 1 + body.length + 1];
        System.arraycopy(jsonpBytes, 0, bodyBs, 0, jsonpBytes.length);
        bodyBs[jsonpBytes.length] = SysConst.LEFT_BRACKET;
        System.arraycopy(body, 0, bodyBs, jsonpBytes.length + 1, body.length);
        bodyBs[bodyBs.length - 1] = SysConst.RIGHT_BRACKET;
        body = bodyBs;
        httpResponse.setBody(bodyBs);
      }
    }

    if (body != null) {

      HeaderValue contentType = httpResponse.getContentType();
      String mime = contentType != null ? contentType.getValue().toLowerCase() : "";
      // skip gzip
      if (mime.startsWith("image/") || mime.startsWith("video/") || mime.startsWith("audio/")) {
        bodyLength = body.length;
      } else {
        // gzip
        if (!httpResponse.hasGzipped()) {
          try {
            HttpGzipUtils.gzip(httpRequest, httpResponse);
            body = httpResponse.body;
          } catch (Exception e) {
            log.error("Failed to gzip body", e);
          }
        }
        bodyLength = body.length;
      }
    }

    HttpResponseStatus httpResponseStatus = httpResponse.getStatus();

    int respLineLength = httpResponseStatus.responseLineBinary.length;

    Map<HeaderName, HeaderValue> headers = httpResponse.getHeaders();
    // Content_Length
    boolean isNotAddContentLength = httpResponse.isStream() || httpResponse.hasCountContentLength();
    if (!isNotAddContentLength) {
      httpResponse.addHeader(HeaderName.Content_Length, HeaderValue.from(Integer.toString(bodyLength)));
    }

    //keep alive
    int headerLength = httpResponse.getHeaderByteCount();
    if (httpResponse.getCookies() != null) {
      for (Cookie cookie : httpResponse.getCookies()) {
        headerLength += HeaderName.SET_COOKIE.bytes.length;
        byte[] bs;
        try {
          bs = cookie.toString().getBytes(httpResponse.getCharset());
        } catch (UnsupportedEncodingException e) {
          throw new RuntimeException(e);
        }
        cookie.setBytes(bs);
        headerLength += (bs.length);
      }
      headerLength += httpResponse.getCookies().size() * 3; // 冒号和\r\n
    }

    HeaderValue httpDateValue = HttpDateTimer.httpDateValue;

    headerLength += HEADER_FIXED_LENGTH + httpDateValue.bytes.length;

    ByteBuffer buffer = ByteBuffer.allocate(respLineLength + headerLength + bodyLength);
    buffer.put(httpResponseStatus.responseLineBinary);

    if (showServer) {
      buffer.put(HeaderName.Server.bytes);
      buffer.put(SysConst.COL);
      buffer.put(HeaderValue.Server.TIO.bytes);
      buffer.put(SysConst.CR_LF);
    }

    buffer.put(HeaderName.Date.bytes);
    buffer.put(SysConst.COL);
    buffer.put(httpDateValue.bytes);
    buffer.put(SysConst.CR_LF);

    Set<Entry<HeaderName, HeaderValue>> headerSet = headers.entrySet();
    for (Entry<HeaderName, HeaderValue> entry : headerSet) {
      buffer.put(entry.getKey().bytes);
      buffer.put(SysConst.COL);
      buffer.put(entry.getValue().bytes);
      buffer.put(SysConst.CR_LF);
    }

    // 处理cookie
    if (httpResponse.getCookies() != null) {
      for (Cookie cookie : httpResponse.getCookies()) {
        buffer.put(HeaderName.SET_COOKIE.bytes);
        buffer.put(SysConst.COL);
        buffer.put(cookie.getBytes());
        buffer.put(SysConst.CR_LF);
      }
    }

    buffer.put(SysConst.CR_LF);

    if (bodyLength > 0) {
      buffer.put(body);
    }
    buffer.flip();
    return buffer;
  }

  private static ByteBuffer buildHeader(HttpResponse httpResponse, long contentLength) {
    HttpResponseStatus status = httpResponse.getStatus();
    byte[] httpLine = status.responseLineBinary;
    Map<HeaderName, HeaderValue> headers = httpResponse.getHeaders();
    HeaderValue dateValue = HttpDateTimer.httpDateValue;

    // 先把 Content-Length 的 bytes 准备好
    byte[] lengthBytes = null;
    try {
      lengthBytes = String.valueOf(contentLength).getBytes(httpResponse.getCharset());
    } catch (UnsupportedEncodingException e1) {
      e1.printStackTrace();
    }

    // 重新计算 header 长度
    int headerLen = 0;
    headerLen += httpLine.length;

    if (showServer) {
      headerLen += HEADER_SERVER_LENGTH;
    }
    headerLen += HEADER_DATE_LENGTH_1 + dateValue.bytes.length;

    // Content-Length
    headerLen += HeaderName.Content_Length.bytes.length + 1 + lengthBytes.length + 2;

    // 其它自定义 headers
    for (Entry<HeaderName, HeaderValue> e : headers.entrySet()) {
      headerLen += e.getKey().bytes.length + 1 + e.getValue().bytes.length + 2;
    }
    // cookies
    if (httpResponse.getCookies() != null) {
      for (Cookie c : httpResponse.getCookies()) {
        byte[] cbytes = c.getBytes();
        headerLen += HeaderName.SET_COOKIE.bytes.length + 1 + cbytes.length + 2;
      }
    }
    // 结尾的 CRLF
    headerLen += 2;

    // 构建 buffer
    ByteBuffer buf = ByteBuffer.allocate(headerLen);
    buf.put(httpLine);

    if (showServer) {
      buf.put(HeaderName.Server.bytes).put((byte) ':').put(HeaderValue.Server.TIO.bytes).put(SysConst.CR_LF);
    }

    // Date 头
    buf.put(HeaderName.Date.bytes).put((byte) ':').put(dateValue.bytes).put(SysConst.CR_LF);

    // **Content-Length 头**（必须在其他无长度提示的场景里先写）
    buf.put(HeaderName.Content_Length.bytes).put((byte) ':').put(lengthBytes).put(SysConst.CR_LF);

    // 其它 headers
    for (Entry<HeaderName, HeaderValue> e : headers.entrySet()) {
      buf.put(e.getKey().bytes).put((byte) ':').put(e.getValue().bytes).put(SysConst.CR_LF);
    }

    // Cookies
    if (httpResponse.getCookies() != null) {
      for (Cookie c : httpResponse.getCookies()) {
        buf.put(HeaderName.SET_COOKIE.bytes).put((byte) ':').put(c.getBytes()).put(SysConst.CR_LF);
      }
    }

    // 头部结束空行
    buf.put(SysConst.CR_LF);
    buf.flip();
    return buf;
  }

}
