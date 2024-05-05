package com.litongjava.tio.http.client;

import com.litongjava.tio.core.Node;
import com.litongjava.tio.http.common.HttpRequest;
import com.litongjava.tio.http.common.Method;
import com.litongjava.tio.http.common.RequestLine;

/**
 * httpclient
 * @author tanyaowu
 *
 */
public class ClientHttpRequest extends HttpRequest {

  /**
   * 
   */
  private static final long serialVersionUID = -1997414964490639641L;

  public ClientHttpRequest(Node remote) {
    super(remote);
  }

  public static ClientHttpRequest get(String path, String queryString) {
    return new ClientHttpRequest(Method.GET, path, queryString);
  }

  public ClientHttpRequest(Method method, String path, String queryString) {
    super();
    RequestLine requestLine = new RequestLine();
    requestLine.setMethod(method);
    requestLine.setPath(path);
    requestLine.setQueryString(queryString);
    requestLine.setProtocol("HTTP");
    requestLine.setVersion("1.1");
    this.setRequestLine(requestLine);
  }

}
