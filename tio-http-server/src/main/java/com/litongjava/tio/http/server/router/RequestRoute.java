package com.litongjava.tio.http.server.router;

import com.litongjava.tio.http.server.handler.HttpRequestRouteHandler;

public interface RequestRoute {

  /**
   * 添加路由
   * @param path
   * @param handler
   */
  public void add(String path, HttpRequestRouteHandler handler);

  /**
   * 查找路由
   * @param path
   * @return
   */
  public HttpRequestRouteHandler find(String path);
}
