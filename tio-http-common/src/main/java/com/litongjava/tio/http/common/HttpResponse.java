package com.litongjava.tio.http.common;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.litongjava.tio.http.common.utils.HttpGzipUtils;
import com.litongjava.tio.utils.SysConst;
import com.litongjava.tio.utils.hutool.ClassUtil;
import com.litongjava.tio.utils.hutool.StrUtil;
import com.litongjava.tio.utils.json.Json;

/**
 * @author tanyaowu
 */
public class HttpResponse extends HttpPacket {
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
  /**
   * 不计算Content-Length
   */
  private boolean hasCountContentLength = false;
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
      this.addHeader(HeaderName.Last_Modified, lastModified);
    }
  }

  @Override
  public String toString() {
    return this.status.toString();
  }

  /**
   * @return the headerByteCount
   */
  public int getHeaderByteCount() {
    return headerByteCount;
  }

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

  public HttpResponse addServerSentEventsHeader(String charset) {
    this.setContentType("text/event-stream;charset=" + charset);
    this.addHeader(HeaderName.Connection, HeaderValue.from("keep-alive"));
    this.stream = true;
    return this;
  }

  public HttpResponse addServerSentEventsHeader() {
    return addServerSentEventsHeader("utf-8");
  }

  public void sendRedirect(String url) {
    setStatus(HttpResponseStatus.C302);
    addHeader(HeaderName.Location, HeaderValue.from(url));

  }

  public static HttpResponse string(String bodyString, String charset, String mimeTypeStr) {
    HttpResponse httpResponse = new HttpResponse();
    httpResponse.setString(bodyString, charset, mimeTypeStr);
    return httpResponse;
  }

  public HttpResponse setString(String bodyString) {
    if (bodyString != null) {
      try {
        setBody(bodyString.getBytes(charset));
      } catch (UnsupportedEncodingException e) {
        throw new RuntimeException(e);
      }
    }
    this.addHeader(HeaderName.Content_Type, HeaderValue.Content_Type.TEXT_PLAIN_TXT);
    return this;
  }

  public HttpResponse setString(String bodyString, String charset, String mimeTypeStr) {
    if (bodyString != null) {
      if (charset == null) {
        setBody(bodyString.getBytes());
      } else {
        try {
          setBody(bodyString.getBytes(charset));
        } catch (UnsupportedEncodingException e) {
          throw new RuntimeException(e);
        }
      }
    }
    this.addHeader(HeaderName.Content_Type, HeaderValue.Content_Type.from(mimeTypeStr));
    return this;
  }

  public HttpResponse setJson(Object body) {
    String charset = this.getHttpRequest().getHttpConfig().getCharset();
    if (body == null) {
      return setString("", charset, getMimeTypeStr(MimeType.TEXT_PLAIN_JSON, charset));
    } else {
      if (body.getClass() == String.class || ClassUtil.isBasicType(body.getClass())) {
        return setString(body + "", charset, getMimeTypeStr(MimeType.TEXT_PLAIN_JSON, charset));
      } else {
        return setString(Json.getJson().toJson(body), charset, getMimeTypeStr(MimeType.TEXT_PLAIN_JSON, charset));
      }
    }
  }

  public HttpResponse fail(Object body) {
    this.setStatus(400);
    return setJson(body);
  }

  public static HttpResponse json(Object body) {
    String charset = Charset.defaultCharset().name();
    return json(body, charset);
  }

  public static HttpResponse json(Object body, String charset) {
    if (body == null) {
      return string("", charset, getMimeTypeStr(MimeType.TEXT_PLAIN_JSON, charset));
    } else {
      if (body.getClass() == String.class || ClassUtil.isBasicType(body.getClass())) {
        return string(body + "", charset, getMimeTypeStr(MimeType.TEXT_PLAIN_JSON, charset));
      } else {
        return string(Json.getJson().toJson(body), charset, getMimeTypeStr(MimeType.TEXT_PLAIN_JSON, charset));
      }
    }
  }

  private static String getMimeTypeStr(MimeType mimeType, String charset) {
    if (charset == null) {
      return mimeType.getType();
    } else {
      return mimeType.getType() + ";charset=" + charset;
    }
  }

  public static HttpResponse json(HttpRequest request, Object body) {
    HttpResponse httpResponse = new HttpResponse(request);
    return httpResponse.setJson(body);
  }

  public boolean isHasCountContentLength() {
    return hasCountContentLength;
  }

  public HttpResponse setHasCountContentLength(boolean b) {
    this.hasCountContentLength = b;
    return this;
  }

  public HttpResponse removeHeaders(String name) {
    headers.remove(HeaderName.from(name));
    return this;

  }
}
