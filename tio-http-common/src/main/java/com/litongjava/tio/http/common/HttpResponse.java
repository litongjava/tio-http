package com.litongjava.tio.http.common;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.litongjava.tio.http.common.utils.HttpGzipUtils;
import com.litongjava.tio.utils.SysConst;
import com.litongjava.tio.utils.hutool.StrUtil;

/**
 *
 * @author tanyaowu
 *
 */
public class HttpResponse extends HttpPacket {
  @SuppressWarnings("unused")
  private static Logger log = LoggerFactory.getLogger(HttpResponse.class);
  private static final long serialVersionUID = -3512681144230291786L;
  public static final HttpResponse NULL_RESPONSE = new HttpResponse();
  /**
   * 服务器端用（因为服务器端可以直接枚举）
   */
  private HttpResponseStatus status = HttpResponseStatus.C200;
  /**
   * 是否是静态资源
   * true: 静态资源
   */
  private boolean isStaticRes = false;
  /**
   * 是否向客户端发送消息,SSE的情况下不发送,由Controller控制具体的方式
   */
  private boolean send = true;
  /**
   * 是否后续返回流格式,如果是则在相应时不计算Content-Length
   */
  private boolean stream = false;
  private HttpRequest request = null;
  private List<Cookie> cookies = null;
  private Map<HeaderName, HeaderValue> headers = new HashMap<>();
  private int headerByteCount = 2;
  /**
   * 是否已经被gzip压缩过了，防止重复压缩
   */
  private boolean hasGzipped = false;
  private String charset = HttpConst.CHARSET_NAME;
  /**
   * 忽略ip访问统计
   */
  private boolean skipIpStat = false;
  /**
   * 忽略token访问统计
   */
  private boolean skipTokenStat = false;

  public HttpResponse() {
  }

  /**
   * 
   * @param request
   */
  public HttpResponse(HttpRequest request) {
    this();
    this.request = request;
    if (request == null) {
      return;
    }

    if (request.httpConfig != null && request.httpConfig.compatible1_0) {
      String connection = request.getConnection();// StrUtil.lowerCase(request.getHeader(HttpConst.RequestHeaderKey.Connection));
      switch (request.requestLine.version) {
      case HttpConst.HttpVersion.V1_0:
        if (StrUtil.equals(connection, HttpConst.RequestHeaderValue.Connection.keep_alive)) {
          addHeader(HeaderName.Connection, HeaderValue.Connection.keep_alive);
          addHeader(HeaderName.Keep_Alive, HeaderValue.Keep_Alive.TIMEOUT_10_MAX_20);
        } else {
          // addHeader(HeaderName.Connection, HeaderValue.Connection.close);
        }
        break;

      default:
        if (StrUtil.equals(connection, HttpConst.RequestHeaderValue.Connection.close)) {
          // addHeader(HeaderName.Connection, HeaderValue.Connection.close);
        } else {
          // addHeader(HeaderName.Connection, HeaderValue.Connection.keep_alive);
          // addHeader(HeaderName.Keep_Alive, HeaderValue.Keep_Alive.TIMEOUT_10_MAX_20);
        }
        break;
      }
    }
  }

  /**
   * 
   * @param responseHeaders
   * @param body
   */
  public HttpResponse(Map<HeaderName, HeaderValue> responseHeaders, byte[] body) {
    if (responseHeaders != null) {
      this.headers.putAll(responseHeaders);
    }
    this.setBody(body);
    HttpGzipUtils.gzip(this);
  }

  /**
   * 支持跨域
   * @author tanyaowu
   */
  public void crossDomain() {
    addHeader(HeaderName.Access_Control_Allow_Origin, HeaderValue.from("*"));
    addHeader(HeaderName.Access_Control_Allow_Headers, HeaderValue.from("x-requested-with,content-type"));
  }

  public static HttpResponse cloneResponse(HttpRequest request, HttpResponse response) {
    HttpResponse cloneResponse = new HttpResponse(request);
    cloneResponse.setStatus(response.getStatus());
    cloneResponse.setBody(response.getBody());
    cloneResponse.setHasGzipped(response.isHasGzipped());
    cloneResponse.addHeaders(response.getHeaders());

    if (cloneResponse.getCookies() != null) {
      cloneResponse.getCookies().clear();
    }
    return cloneResponse;
  }

  /**
   * <span style='color:red'>
   *  <p style='color:red;font-size:12pt;'>警告：通过本方法获得Map<HeaderName, HeaderValue>对象后，请勿调用put(key, value)。<p>
   *  <p style='color:red;font-size:12pt;'>添加响应头只能通过HttpResponse.addHeader(HeaderName, HeaderValue)或HttpResponse.addHeaders(Map<HeaderName, HeaderValue> headers)方式添加<p>
   * </span>
   * @return
   * @author tanyaowu
   */
  public Map<HeaderName, HeaderValue> getHeaders() {
    return headers;
  }

  // private String lastModified = null;//HttpConst.ResponseHeaderKey.Last_Modified

  // /**
  // *
  // * @param request
  // * @param httpConfig 可以为null
  // * @author tanyaowu
  // */
  // public HttpResponse(HttpRequest request, HttpConfig httpConfig) {
  // this.request = request;
  //
  // String Connection = StrUtil.lowerCase(request.getHeader(HttpConst.RequestHeaderKey.Connection));
  // RequestLine requestLine = request.getRequestLine();
  // String version = requestLine.getVersion();
  // if ("1.0".equals(version)) {
  // if (StrUtil.equals(Connection, HttpConst.RequestHeaderValue.Connection.keep_alive)) {
  // addHeader(HttpConst.ResponseHeaderKey.Connection, HttpConst.ResponseHeaderValue.Connection.keep_alive);
  // addHeader(HttpConst.ResponseHeaderKey.Keep_Alive, "timeout=10, max=20");
  // } else {
  // addHeader(HttpConst.ResponseHeaderKey.Connection, HttpConst.ResponseHeaderValue.Connection.close);
  // }
  // } else {
  // if (StrUtil.equals(Connection, HttpConst.RequestHeaderValue.Connection.close)) {
  // addHeader(HttpConst.ResponseHeaderKey.Connection, HttpConst.ResponseHeaderValue.Connection.close);
  // } else {
  // addHeader(HttpConst.ResponseHeaderKey.Connection, HttpConst.ResponseHeaderValue.Connection.keep_alive);
  // addHeader(HttpConst.ResponseHeaderKey.Keep_Alive, "timeout=10, max=20");
  // }
  // }
  //
  //
  // if (httpConfig != null) {
  // addHeader(HttpConst.ResponseHeaderKey.Server, httpConfig.getServerInfo());
  // }
  // // String xx = DatePattern.HTTP_DATETIME_FORMAT.format(SystemTimer.currTime);
  // // addHeader(HttpConst.ResponseHeaderKey.Date, DatePattern.HTTP_DATETIME_FORMAT.format(SystemTimer.currTime));
  // // addHeader(HttpConst.ResponseHeaderKey.Date, new Date().toGMTString());
  // }

  public void addHeader(HeaderName key, HeaderValue value) {
    headers.put(key, value);
    headerByteCount += (key.bytes.length + value.bytes.length + 3); // 冒号和\r\n
  }

  public void addHeader(String name, String headeValue) {
    HeaderName key = HeaderName.from(name);
    HeaderValue value = HeaderValue.from(headeValue);
    headers.put(key, value);
    headerByteCount += (key.bytes.length + value.bytes.length + 3); // 冒号和\r\n
  }

  public void setHeader(String name, String value) {
    this.addHeader(name, value);
  }

  public void addHeaders(Map<HeaderName, HeaderValue> headers) {
    if (headers != null) {
      Set<Entry<HeaderName, HeaderValue>> set = headers.entrySet();
      for (Entry<HeaderName, HeaderValue> entry : set) {
        this.addHeader(entry.getKey(), entry.getValue());
      }
    }
  }

  /**
   * 获取"Content-Type"头部内容
   * @return
   * @author tanyaowu
   */
  public HeaderValue getContentType() {
    return this.headers.get(HeaderName.Content_Type);
  }

  public boolean addCookie(Cookie cookie) {
    if (cookies == null) {
      cookies = new ArrayList<>();
    }
    return cookies.add(cookie);
  }

  /**
   * @return the charset
   */
  public String getCharset() {
    return charset;
  }

  /**
   * @return the cookies
   */
  public List<Cookie> getCookies() {
    return cookies;
  }

  // /**
  // * @return the encodedBytes
  // */
  // public byte[] getEncodedBytes() {
  // return encodedBytes;
  // }

  /**
   * @return the request
   */
  public HttpRequest getHttpRequest() {
    return request;
  }

  /**
   * @return the status
   */
  public HttpResponseStatus getStatus() {
    return status;
  }

  /**
   * @return the isStaticRes
   */
  public boolean isStaticRes() {
    return isStaticRes;
  }

  @Override
  public String logstr() {
    String str = null;
    if (request != null) {
      str = "reponse: requestID_" + request.getId() + "  " + request.getRequestLine().getPathAndQuery();
      str += SysConst.CRLF + this.getHeaderString();
    } else {
      str = "nresponse " + status.getHeaderText();
    }
    return str;
  }

  /**
   * @param charset the charset to set
   */
  public void setCharset(String charset) {
    this.charset = charset;
  }

  /**
   * @param cookies the cookies to set
   */
  public void setCookies(List<Cookie> cookies) {
    this.cookies = cookies;
  }

  // /**
  // * @param encodedBytes the encodedBytes to set
  // */
  // public void setEncodedBytes(byte[] encodedBytes) {
  // this.encodedBytes = encodedBytes;
  // }

  /**
   * @param request the request to set
   */
  public void setHttpRequestPacket(HttpRequest request) {
    this.request = request;
  }

  /**
   * @param isStaticRes the isStaticRes to set
   */
  public void setStaticRes(boolean isStaticRes) {
    this.isStaticRes = isStaticRes;
  }

  /**
   * @param status the status to set
   */
  public void setStatus(HttpResponseStatus status) {
    this.status = status;
  }

  public void setStatus(int status, String description, String headerText) {
    HttpResponseStatus custom = HttpResponseStatus.CUSTOM.build(status, description, headerText);
    this.status = custom;
  }

  public void setStatus(int status, String description) {
    HttpResponseStatus custom = HttpResponseStatus.CUSTOM.build(status, description);
    this.status = custom;
  }

  public void setStatus(int status) {
    HttpResponseStatus custom = HttpResponseStatus.CUSTOM.build(status);
    this.status = custom;
  }

  public boolean isHasGzipped() {
    return hasGzipped;
  }

  public void setHasGzipped(boolean hasGzipped) {
    this.hasGzipped = hasGzipped;
  }

  public boolean isSkipIpStat() {
    return skipIpStat;
  }

  public void setSkipIpStat(boolean skipIpStat) {
    this.skipIpStat = skipIpStat;
  }

  public boolean isSkipTokenStat() {
    return skipTokenStat;
  }

  public void setSkipTokenStat(boolean skipTokenStat) {
    this.skipTokenStat = skipTokenStat;
  }

  public HeaderValue getLastModified() {
    // if (lastModified != null) {
    // return lastModified;
    // }
    return this.getHeader(HeaderName.Last_Modified);
  }

  /**
   * 
   * @param name 从HeaderName中找，或者HeaderName.from(name)
   * @return
   * @author tanyaowu
   */
  public HeaderValue getHeader(HeaderName name) {
    return headers.get(name);
  }

  public void setLastModified(HeaderValue lastModified) {
    if (lastModified != null) {
      // this.lastModified = lastModified;
      this.addHeader(HeaderName.Last_Modified, lastModified);
    }
  }

  @Override
  public String toString() {
    // String ret = this.getHeaderString();
    // if (this.getBody() != null) {
    // try {
    // ret += new String(this.getBody(), this.request.getCharset());
    // } catch (UnsupportedEncodingException e) {
    // log.error(e.toString(), e);
    // }
    // }
    return this.status.toString();
  }

  /**
   * @return the headerByteCount
   */
  public int getHeaderByteCount() {
    return headerByteCount;
  }
  // /**
  // * @return the cookieByteCount
  // */
  // public int getCookieByteCount() {
  // return cookieByteCount;
  // }

  public void setContentType(String contentType) {
    this.addHeader(HeaderName.Content_Type, HeaderValue.from(contentType));
  }

  public boolean isSend() {
    return send;
  }

  public HttpResponse setSend(boolean send) {
    this.send = send;
    return this;
  }

  public boolean isStream() {
    return stream;
  }

  public void setStream(boolean stream) {
    this.stream = stream;
  }

  public HttpResponse setServerSentEventsHeader() {
    this.setContentType("text/event-stream");
    this.addHeader(HeaderName.Connection, HeaderValue.from("keep-alive"));
    this.addHeader(HeaderName.Date, HeaderValue.from(new Date().toString()));
    this.addHeader(HeaderName.Keep_Alive, HeaderValue.from("timeout=60"));
    this.stream = true;
    return this;

  }

  public void sendRedirect(String url) {
    setStatus(HttpResponseStatus.C302);
    addHeader(HeaderName.Location, HeaderValue.from(url));

  }
}
