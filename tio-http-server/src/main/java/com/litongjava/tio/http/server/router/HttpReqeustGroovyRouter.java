package com.litongjava.tio.http.server.router;

import com.litongjava.tio.http.server.handler.IHttpRequestHandler;

/**
 * 从数据库中加载路由
 * @author Tong Li
 *
 */
public interface HttpReqeustGroovyRouter {

  /**
   * 添加路由
   * @param path
   * @param handler
   */
  public void add(String path, IHttpRequestHandler handler);

  /**
   * 查找路由
   * @param path
   * @return
   */
  public IHttpRequestHandler find(String path);
}
