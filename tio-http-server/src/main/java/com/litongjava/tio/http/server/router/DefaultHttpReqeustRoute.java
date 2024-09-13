package com.litongjava.tio.http.server.router;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.litongjava.tio.http.server.handler.IHttpRequestHandler;

public class DefaultHttpReqeustRoute implements HttpRequestRouter {
  Map<String, IHttpRequestHandler> requestMapping = new ConcurrentHashMap<>();

  public void add(String path, IHttpRequestHandler handler) {
    requestMapping.put(path, handler);
  }

  /**
   * find route
   * /* 表示匹配任何以特定路径开始的路径，/** 表示匹配该路径及其下的任何子路径
   */
  public IHttpRequestHandler find(String path) {
    IHttpRequestHandler httpRequestRouteHandler = requestMapping.get(path);
    if (httpRequestRouteHandler != null) {
      return httpRequestRouteHandler;
    }

    // Check for wildcard matches
    Set<Map.Entry<String, IHttpRequestHandler>> entrySet = requestMapping.entrySet();

    for (Map.Entry<String, IHttpRequestHandler> entry : entrySet) {
      String key = entry.getKey();

      if (key.endsWith("/*")) {
        String baseRoute = key.substring(0, key.length() - 1);
        if (path.startsWith(baseRoute)) {
          return entry.getValue();
        }
      } else if (key.endsWith("/**")) {
        String baseRoute = key.substring(0, key.length() - 2);
        if (path.startsWith(baseRoute)) {
          return entry.getValue();
        }
      }
    }

    return null;

  }

}
