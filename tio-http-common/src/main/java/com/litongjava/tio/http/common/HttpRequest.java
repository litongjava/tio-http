package com.litongjava.tio.http.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.litongjava.model.sys.SysConst;
import com.litongjava.tio.consts.TioConst;
import com.litongjava.tio.core.ChannelContext;
import com.litongjava.tio.core.Node;
import com.litongjava.tio.core.Tio;
import com.litongjava.tio.http.common.HttpConst.RequestBodyFormat;
import com.litongjava.tio.http.common.session.HttpSession;
import com.litongjava.tio.utils.SystemTimer;
import com.litongjava.tio.utils.hutool.StrUtil;

/**
 * @author tanyaowu
 */
public class HttpRequest extends HttpPacket {
  private static Logger log = LoggerFactory.getLogger(HttpRequest.class);
  private static final long serialVersionUID = -3849253977016967211L;
  private boolean needForward = false;
  private boolean isForward = false;
  public RequestLine requestLine = null;
  /**
   * 请求参数
   */
  private Map<String, Object[]> params = new HashMap<>();
  private List<Cookie> cookies = null;
  private Map<String, Cookie> cookieMap = null;
  private int contentLength;
  private String connection;
  private String bodyString;
  private RequestBodyFormat bodyFormat;

  private String charset = TioConst.CHARSET_NAME;

  private Boolean isAjax = null;
  @SuppressWarnings("unused")
  private Boolean isSupportGzip = null;
  private HttpSession httpSession;
  private Node remote = null;
  public ChannelContext channelContext;
  public HttpConfig httpConfig;
  private String domain = null;
  private String host = null;

  /**该HttpRequest对象的创建时间*/
  private long createTime = SystemTimer.currTime;
  private boolean closed = false;
  protected Map<String, String> headers = new HashMap<>();
  private Integer forwardCount = null;

  public HttpRequest(Node remote) {
    this.remote = remote;
  }

  public HttpRequest() {
  }

  /**
   * 关闭连接
   */
  public void close() {
    close(null);
  }

  /**
   * 关闭连接
   * @param remark
   */
  public void close(String remark) {
    closed = true;
    Tio.remove(channelContext, remark);
  }

  public void addParam(String key, Object value) {
    if (value == null) {
      return;
    }
    Object[] existValue = params.get(key);
    if (existValue != null) {
      Object[] newExistValue = new Object[existValue.length + 1];
      if (value instanceof String) {
        newExistValue = new String[existValue.length + 1];
      } else if (value instanceof UploadFile) {
        newExistValue = new UploadFile[existValue.length + 1];
      }
      System.arraycopy(existValue, 0, newExistValue, 0, existValue.length);
      newExistValue[newExistValue.length - 1] = value;
      params.put(key, newExistValue);
    } else {
      Object[] newExistValue = null;// new Object[] { value };
      if (value instanceof String) {
        newExistValue = new String[] { (String) value };
      } else if (value instanceof UploadFile) {
        newExistValue = new UploadFile[] { (UploadFile) value };
      }
      params.put(key, newExistValue);
    }
  }

  /**
   * 
   * @param newPath
   * @return
   * @throws Exception
   */
  public HttpResponse forward(String newPath) throws Exception {
    if (StrUtil.contains(newPath, '?')) {
      requestLine.path = StrUtil.subBefore(newPath, "?", false);
      requestLine.queryString = StrUtil.subAfter(newPath, "?", false);
    } else {
      requestLine.path = newPath;
      requestLine.queryString = null;
    }

    if (forwardCount == null) {
      forwardCount = 1;
    } else {
      forwardCount++;
    }
    if (forwardCount > httpConfig.maxForwardCount) {
      log.error("forwardCount[{}] is too large, newPath:{}", forwardCount, newPath);
      this.close();
      return null;
    }

    this.needForward = true;

    return HttpResponse.NULL_RESPONSE;

  }

  /**
   * @return the bodyFormat
   */
  public RequestBodyFormat getBodyFormat() {
    return bodyFormat;
  }

  /**
   * @param userAgent the userAgent to set
   */
  public void setUserAgent(String userAgent) {
    this.headers.put(RequestHeaderKey.User_Agent, userAgent);
  }

  /**
   * 获取请求头中的User-Agent字段
   * @return
   * @author: tanyaowu
   */
  public String getUserAgent() {
    return this.headers.get(RequestHeaderKey.User_Agent);
  }

  /**
   * 获取请求头中的host字段，形如：www.t-io.org:8080, www.t-io.org等值
   * @return
   * @author: tanyaowu
   */
  public String getHost() {
    if (host != null) {
      return host;
    }

    host = this.headers.get(RequestHeaderKey.Host);
    return host;
  }

  public String getAuthorization() {
    return this.headers.get(RequestHeaderKey.Authorization);
  }

  public String getReferer() {
    return getHeader(RequestHeaderKey.Referer);
  }

  public String getOrigin() {
    return getHeader(RequestHeaderKey.Origin);
  }

  /**
   * 获取真实的客户端ip
   * @return
   * @author tanyaowu
   */
  public String getClientIp() {
    return remote.getIp();
  }

  public void addHeader(String key, String value) {
    headers.put(key, value);
  }

  public void addHeaders(Map<String, String> headers) {
    if (headers != null) {
      Set<Entry<String, String>> set = headers.entrySet();
      for (Entry<String, String> entry : set) {
        this.addHeader(entry.getKey(), entry.getValue());
      }
    }
  }

  /**
   * 根据host字段，获取去除端口的纯域名部分的值，形如：www.t-io.org, t-io.org等值
   * @return
   * @author tanyaowu
   */
  public String getDomain() {
    if (domain != null) {
      return domain;
    }
    if (StrUtil.isBlank(getHost())) {
      return null;
    }
    domain = StrUtil.subBefore(getHost(), ":", false);
    return domain;
  }

  /**
   * @return the bodyString
   */
  public String getBodyString() {
    return bodyString;
  }

  /**
   * @return the channelContext
   */
  public ChannelContext getChannelContext() {
    return channelContext;
  }

  /**
   * @return the charset
   */
  public String getCharset() {
    return charset;
  }

  /**
   * @return the bodyLength
   */
  public int getContentLength() {
    return contentLength;
  }

  public Cookie getCookie(String cooiename) {
    if (cookieMap == null) {
      return null;
    }
    return cookieMap.get(cooiename);
  }

  /**
   * @return the cookieMap
   */
  public Map<String, Cookie> getCookieMap() {
    return cookieMap;
  }

  // /**
  // * @return the bodyBytes
  // */
  public byte[] getBodyBytes() {
    return body;
  }

  /**
  * @param bodyBytes the bodyBytes to set
  */
  public void setBodyBytes(byte[] bodyBytes) {
    this.body = bodyBytes;
  }

  /**
   * @return the cookies
   */
  public List<Cookie> getCookies() {
    return cookies;
  }

  /**
   * @return the cookies
   */
  public Cookie[] getCookiesArray() {
    if (cookies == null) {
      return new Cookie[] {};
    } else {
      return cookies.toArray(new Cookie[] {});
    }
  }

  /**
   * @return the httpConfig
   */
  public HttpConfig getHttpConfig() {
    return httpConfig;
  }

  /**
   * @return the httpSession
   */
  public HttpSession getHttpSession() {
    return httpSession;
  }

  public String getHeader(String key) {
    return headers.get(key);
  }

  /**
   * @return the headers
   */
  public Map<String, String> getHeaders() {
    return headers;
  }

  public void removeHeader(String key, String value) {
    headers.remove(key);
  }

  /**
   * @return the isAjax
   */
  public Boolean getIsAjax() {
    if (isAjax == null) {
      String X_Requested_With = this.getHeader(RequestHeaderKey.X_Requested_With);
      if (X_Requested_With != null && "XMLHttpRequest".equalsIgnoreCase(X_Requested_With)) {
        isAjax = true;
      } else {
        isAjax = false;
      }
    }

    return isAjax;
  }

  /**
   * @return the isSupportGzip
   */
  public Boolean getIsSupportGzip() {
    return true;
  }

  /**
   * @return the params
   */
  public Map<String, Object[]> getParams() {
    return params;
  }

  /**
   * 把类型为数组的参数值转换成Object，相当于是取了数组的第一个值，便于业务开发（因为大部分参数值其实只有一个）
   * @return
   */
  public Map<String, Object> getParam() {
    Map<String, Object> params = new HashMap<>();
    if (getParams() != null) {
      for (String key : this.params.keySet()) {
        Object[] param = this.params.get(key);
        if (param != null && param.length >= 1) {
          params.put(key, param[0]);
        }
      }
    }
    return params;
  }

  public Object getObject(String name) {
    if (StrUtil.isBlank(name)) {
      return null;
    }

    Object[] values = params.get(name);
    if (values != null && values.length > 0) {
      Object obj = values[0];
      return obj;
    }
    return null;
  }

  /**
   * 
   * @param value
   * @return
   * @author: tanyaowu
   */
  public String getParam(String name) {
    return (String) getObject(name);
  }

  /**
   * 同getParam(String name)
   * @param name
   * @return
   * @author tanyaowu
   */
  public String getString(String name) {
    return getParam(name);
  }

  /**
   * 
   * @param name
   * @return
   * @author tanyaowu
   */
  public UploadFile getUploadFile(String name) {
    Object[] values = params.get(name);
    if (values != null && values.length > 0) {
      Object obj = values[0];
      return (UploadFile) obj;
    }
    return null;
  }

  /**
   * 
   * @param name
   * @return
   * @author tanyaowu
   */
  public Integer getInt(String name) {
    String value = getParam(name);
    if (StrUtil.isBlank(value)) {
      return null;
    }

    return Integer.parseInt(value);
  }

  public Short getShort(String name) {
    String value = getParam(name);
    if (StrUtil.isBlank(value)) {
      return null;
    }

    return Short.parseShort(value);
  }

  public Byte getByte(String name) {
    String value = getParam(name);
    if (StrUtil.isBlank(value)) {
      return null;
    }

    return Byte.parseByte(value);
  }

  public Long getLong(String name) {
    String value = getParam(name);
    if (StrUtil.isBlank(value)) {
      return null;
    }

    return Long.parseLong(value);
  }

  public Double getDouble(String name) {
    String value = getParam(name);
    if (StrUtil.isBlank(value)) {
      return null;
    }

    return Double.parseDouble(value);
  }

  public Float getFloat(String name) {
    String value = getParam(name);
    if (StrUtil.isBlank(value)) {
      return null;
    }

    return Float.parseFloat(value);
  }

  public Boolean getBoolean(String name) {
    String value = getParam(name);
    if (StrUtil.isBlank(value)) {
      return null;
    }

    return Boolean.valueOf(value);
  }

  /**
   * 
   * @param name
   * @return
   * @author tanyaowu
   */
  public Object[] getParamArray(String name) {
    Object[] values = params.get(name);
    return values;
  }

  public Node getRemote() {
    return remote;
  }

  /**
   * @return the firstLine
   */
  public RequestLine getRequestLine() {
    return requestLine;
  }

  /**
   * @return
   * @author tanyaowu
   */
  @Override
  public String logstr() {
    String str = "request ID_" + getId() + SysConst.CRLF + getHeaderString();
    if (null != getBodyString()) {
      str += getBodyString();
    }
    return str;
  }

  public void parseCookie(HttpConfig httpConfig) {
    String cookieline = headers.get(RequestHeaderKey.Cookie);
    if (StrUtil.isNotBlank(cookieline)) {
      cookies = new ArrayList<>();
      cookieMap = new HashMap<>();
      Map<String, String> _cookiemap = Cookie.getEqualMap(cookieline);
      Set<Entry<String, String>> set = _cookiemap.entrySet();
      List<Map<String, String>> cookieListMap = new ArrayList<>();
      for (Entry<String, String> cookieMapEntry : set) {
        HashMap<String, String> cookieOneMap = new HashMap<>(1);
        cookieOneMap.put(cookieMapEntry.getKey(), cookieMapEntry.getValue());
        cookieListMap.add(cookieOneMap);

        Cookie cookie = Cookie.buildCookie(cookieOneMap, httpConfig);
        cookies.add(cookie);
        cookieMap.put(cookie.getName(), cookie);
        // log.error("{}, 收到cookie:{}", channelContext, cookie.toString());
      }
    }
  }

  /**
   * @param bodyFormat the bodyFormat to set
   */
  public void setBodyFormat(RequestBodyFormat bodyFormat) {
    this.bodyFormat = bodyFormat;
  }

  /**
   * @param bodyString the bodyString to set
   */
  public void setBodyString(String bodyString) {
    this.bodyString = bodyString;
  }

  /**
   * @param channelContext the channelContext to set
   */
  public void setChannelContext(ChannelContext channelContext) {
    this.channelContext = channelContext;
    this.charset = channelContext.getTioConfig().getCharset();
  }

  /**
   * @param charset the charset to set
   */
  public void setCharset(String charset) {
    this.charset = charset;
  }

  /**
   * @param bodyLength the bodyLength to set
   */
  public void setContentLength(int contentLength) {
    this.contentLength = contentLength;
  }

  /**
   * @param cookieMap the cookieMap to set
   */
  public void setCookieMap(Map<String, Cookie> cookieMap) {
    this.cookieMap = cookieMap;
  }

  /**
   * @param cookies the cookies to set
   */
  public void setCookies(List<Cookie> cookies) {
    this.cookies = cookies;
  }

  /**
   * 设置好header后，会把cookie等头部信息也设置好
   * @param headers the headers to set
   * @param channelContext
   */
  public void setHeaders(Map<String, String> headers) {
    this.headers = headers;
    if (headers != null) {
      parseCookie(httpConfig);
    }
  }

  /**
   * @param httpConfig the httpConfig to set
   */
  public void setHttpConfig(HttpConfig httpConfig) {
    this.httpConfig = httpConfig;
  }

  /**
   * @param httpSession the httpSession to set
   */
  public void setHttpSession(HttpSession httpSession) {
    this.httpSession = httpSession;
  }

  /**
   * @param isAjax the isAjax to set
   */
  public void setIsAjax(Boolean isAjax) {
    this.isAjax = isAjax;
  }

  /**
   * @param isSupportGzip the isSupportGzip to set
   */
  public void setIsSupportGzip(Boolean isSupportGzip) {
    this.isSupportGzip = isSupportGzip;
  }

  /**
   * @param params the params to set
   */
  public void setParams(Map<String, Object[]> params) {
    this.params = params;
  }

  public void setRemote(Node remote) {
    this.remote = remote;
  }

  /**
   * @param requestLine the requestLine to set
   */
  public void setRequestLine(RequestLine requestLine) {
    this.requestLine = requestLine;
  }

  public long getCreateTime() {
    return createTime;
  }

  public void setCreateTime(long createTime) {
    this.createTime = createTime;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(this.requestLine.toString());

    if (this.getHeaderString() != null) {
      sb.append(this.getHeaderString()).append(SysConst.CRLF);
    }

    if (this.getBodyString() != null) {
      sb.append(this.getBodyString());
    }

    return sb.toString();
  }

  public boolean isClosed() {
    return closed;
  }

  public void setClosed(boolean closed) {
    this.closed = closed;
  }

  /**
   * @return the connection
   */
  public String getConnection() {
    return connection;
  }

  /**
   * @param connection the connection to set
   */
  public void setConnection(String connection) {
    this.connection = connection;
  }

  public boolean isNeedForward() {
    return needForward;
  }

  public void setNeedForward(boolean needForward) {
    this.needForward = needForward;
  }

  public boolean isForward() {
    return isForward;
  }

  public void setForward(boolean isForward) {
    this.isForward = isForward;
  }

  public String getContentType() {
    return this.getHeader(RequestHeaderKey.Content_Type);
  }

  public Enumeration<String> getParameterNames() {
    return Collections.enumeration(params.keySet());
  }

  public String getParameter(String paramName) {
    return getParam(paramName);
  }

  public Map<String, Object[]> getParameterMap() {
    return this.getParams();
  }

  public String getRequestURI() {
    String path = requestLine.getPath();
    return path;
  }

  public StringBuffer getRequestURL() {
    StringBuffer stringBuffer = new StringBuffer();
    stringBuffer.append(requestLine.getProtocol().toLowerCase()).append("://").append(host).append(requestLine.getPath());
    return stringBuffer;
  }

  /**
   * eg:GET
   * @return
   */
  public HttpMethod getMethod() {
    return requestLine.getMethod();
  }

  public RequestDispatcher getRequestDispatcher(String path) {
    return new RequestDispatcher(path);
  }

  public void setUserId(Object userId) {
    this.setAttribute("userId", userId);
  }

  public Object getUserId() {
    return this.getAttribute("userId");
  }

  public String getUserIdString() {
    Object attribute = this.getUserId();
    if (attribute != null) {
      if (attribute instanceof String) {
        return (String) attribute;
      } else {
        return attribute.toString();
      }
    }
    return null;
  }
  
  public Long getUserIdLong() {
    Object attribute = this.getAttribute("userId");
    if (attribute != null) {
      if (attribute instanceof Long) {
        return (Long) attribute;
      } else {
        return Long.valueOf((String) attribute);
      }
    }
    return null;

  }

}
