package com.litongjava.tio.http.server.router;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.litongjava.tio.http.server.handler.HttpRequestHandler;

public class DefaultHttpReqeustRouter implements HttpRequestRouter {
  Map<String, HttpRequestHandler> requestMapping = new ConcurrentHashMap<>();

  public void add(String path, HttpRequestHandler handler) {
    requestMapping.put(path, handler);
  }

  /**
   * find route /* 表示匹配任何以特定路径开始的路径，/** 表示匹配该路径及其下的任何子路径
   */
  public HttpRequestHandler find(String path) {
    HttpRequestHandler httpRequestRouteHandler = requestMapping.get(path);
    if (httpRequestRouteHandler != null) {
      return httpRequestRouteHandler;
    }

    // Check for wildcard matches
    Set<Map.Entry<String, HttpRequestHandler>> entrySet = requestMapping.entrySet();

    for (Map.Entry<String, HttpRequestHandler> entry : entrySet) {
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

  @Override
  public Map<String, HttpRequestHandler> all() {
    return requestMapping;
  }

}
