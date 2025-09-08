package com.litongjava.tio.http.server.router;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.litongjava.tio.http.common.HttpRequest;
import com.litongjava.tio.http.server.handler.HttpRequestHandler;

public class DefaultHttpRequestRouter implements HttpRequestRouter {
  Map<String, HttpRequestHandler> requestMapping = new ConcurrentHashMap<>();

  private final List<Route> templateRoutes = Collections.synchronizedList(new ArrayList<>());

  @Override
  public void add(String path, HttpRequestHandler handler) {
    if (path.contains("{") && path.contains("}")) {
      templateRoutes.add(compileTemplate(path, handler));
    } else {
      requestMapping.put(path, handler);
    }
  }

  private Route compileTemplate(String template, HttpRequestHandler handler) {
    List<String> varNames = new ArrayList<>();
    StringBuilder regex = new StringBuilder("^");
    for (int i = 0; i < template.length();) {
      char c = template.charAt(i);
      if (c == '{') {
        int end = template.indexOf('}', i);
        if (end < 0)
          throw new IllegalArgumentException("Unmatched { in route: " + template);
        String inside = template.substring(i + 1, end).trim(); // name 或 name:regex
        String name, expr;
        int colon = inside.indexOf(':');
        if (colon >= 0) {
          name = inside.substring(0, colon).trim();
          expr = inside.substring(colon + 1).trim();
        } else {
          name = inside;
          expr = "[^/]+";
        }
        if (name.isEmpty())
          throw new IllegalArgumentException("Empty variable in route: " + template);
        varNames.add(name);
        regex.append("(").append(expr).append(")");
        i = end + 1;
      } else {
        if ("\\.[]{}()+-^$|".indexOf(c) >= 0)
          regex.append('\\');
        regex.append(c);
        i++;
      }
    }
    regex.append("$");
    return new Route(template, Pattern.compile(regex.toString()), varNames, handler);
  }

  @Override
  public HttpRequestHandler find(String path) {
    HttpRequestHandler direct = requestMapping.get(path);
    if (direct != null)
      return direct;

    for (Map.Entry<String, HttpRequestHandler> entry : requestMapping.entrySet()) {
      String key = entry.getKey();
      if (key.endsWith("/*")) {
        String base = key.substring(0, key.length() - 1);
        if (path.startsWith(base))
          return entry.getValue();
      } else if (key.endsWith("/**")) {
        String base = key.substring(0, key.length() - 2);
        if (path.startsWith(base))
          return entry.getValue();
      }
    }
    return null;
  }

  @Override
  public HttpRequestHandler resolve(HttpRequest request) {
    String path = request.getRequestURI();

    // 精确/通配符
    HttpRequestHandler handler = find(path);
    if (handler != null) {
      return handler;
    }

    // 模板匹配并注入参数
    synchronized (templateRoutes) {
      for (Route r : templateRoutes) {
        Matcher m = r.pattern.matcher(path);
        if (m.matches()) {
          for (int i = 0; i < r.varNames.size(); i++) {
            String name = r.varNames.get(i);
            String value = m.group(i + 1);
            request.addParam(name, value);
          }
          return r.handler;
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
