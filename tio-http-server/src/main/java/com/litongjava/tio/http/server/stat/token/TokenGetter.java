package com.litongjava.tio.http.server.stat.token;

import com.litongjava.tio.http.common.HttpRequest;

/**
 * @author tanyaowu
 *
 */
public interface TokenGetter {

  /**
   * 根据HttpRequest对象获取业务上的token
   * @param request
   * @return
   */
  public String getToken(HttpRequest request);
}
