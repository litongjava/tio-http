package com.litongjava.tio.http.common;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

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

  /**
   *
   * @param httpResponse
   * @param tioConfig
   * @param channelContext
   * @return
   * @author tanyaowu
   */
  public static ByteBuffer encode(HttpResponse httpResponse, TioConfig tioConfig, ChannelContext channelContext) {
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
      // 处理gzip
      if (!httpResponse.hasGzipped()) {
        try {
          HttpGzipUtils.gzip(httpRequest, httpResponse);
          body = httpResponse.body;
        } catch (Exception e) {
          log.error(e.toString(), e);
        }
      }
      bodyLength = body.length;
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

    boolean showServer = EnvUtils.getBoolean("http.response.header.showServer", true);
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

  /**
   *
   *
   * @author tanyaowu
   */
  private HttpResponseEncoder() {

  }
}
