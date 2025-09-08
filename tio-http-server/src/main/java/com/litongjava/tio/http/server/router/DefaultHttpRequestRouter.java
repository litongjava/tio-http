package com.litongjava.tio.http.server.router;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Pattern;

import com.litongjava.tio.http.common.HttpRequest;
import com.litongjava.tio.http.server.handler.HttpRequestHandler;

public class DefaultHttpRequestRouter implements HttpRequestRouter {
  /** 精确/通配符路由 */
  private final Map<String, HttpRequestHandler> requestMapping = new ConcurrentHashMap<>();

  /** 模板路由快照（读多写少） */
  private final CopyOnWriteArrayList<Route> templateRoutes = new CopyOnWriteArrayList<>();

  /** 段数 -> 路由列表（读路径无锁） */
  private final ConcurrentHashMap<Integer, CopyOnWriteArrayList<Route>> routesBySegments = new ConcurrentHashMap<>();

  /** (段数|首静态段) -> 路由列表（更窄的候选集） */
  private final ConcurrentHashMap<String, CopyOnWriteArrayList<Route>> routesByKey = new ConcurrentHashMap<>();

  /** 路由命中缓存（仅缓存路由选择，不缓存参数值） */
  private final ConcurrentHashMap<String, Route> routeHitCache = new ConcurrentHashMap<>(1024);

  @Override
  public void add(String path, HttpRequestHandler handler) {
    if (path.contains("{") && path.contains("}")) {
      Route r = compileTemplate(path, handler);
      templateRoutes.add(r);

      // 段数索引
      routesBySegments.computeIfAbsent(r.segmentsCount, k -> new CopyOnWriteArrayList<>()).add(r);

      // (段数|首静态段) 索引（存在首静态段才建 key）
      if (r.firstLiteral != null) {
        String key = keyOf(r.segmentsCount, r.firstLiteral);
        routesByKey.computeIfAbsent(key, k -> new CopyOnWriteArrayList<>()).add(r);
      }
    } else {
      requestMapping.put(path, handler);
    }
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
    final String path = request.getRequestURI();

    // 1) 先走精确/通配符
    HttpRequestHandler h = find(path);
    if (h != null)
      return h;

    // 2) 命中缓存（再次校验并注入，避免脏缓存）
    Route cached = routeHitCache.get(path);
    if (cached != null) {
      String[] segs = fastSplit(path);
      if (matchAndInject(cached, segs, request)) {
        return cached.handler;
      }
    }

    // 3) 计算候选集
    String[] segs = fastSplit(path);
    int n = segs.length;
    HttpRequestHandler handler = null;

    // 首段字符串（用于与首静态段相等的模板做快速定位）
    String firstSeg = (n > 0 ? segs[0] : "");

    List<Route> candidates = routesByKey.get(keyOf(n, firstSeg));
    if (candidates == null) {
      candidates = routesBySegments.get(n);
    }
    if (candidates == null) {
      candidates = templateRoutes; // 极少走到
    }

    // 4) 分段匹配（常量比较 + 少数小正则）
    for (Route r : candidates) {
      if (matchAndInject(r, segs, request)) {
        handler = r.handler;
        routeHitCache.put(path, r); // 仅缓存路由选择
        break;
      }
    }
    return handler;
  }

  @Override
  public Map<String, HttpRequestHandler> all() {
    return requestMapping;
  }

  /** 将模板编译为分段结构。支持 {name} 与 {name:regex} */
  private Route compileTemplate(String template, HttpRequestHandler handler) {
    // 以 / 分段（不使用正则 split）
    String[] tSegs = fastSplit(template);
    int n = tSegs.length;

    String[] segLiterals = new String[n];
    String[] varNames = new String[n];
    Pattern[] varPatterns = new Pattern[n];

    String firstLiteral = null;

    for (int i = 0; i < n; i++) {
      String seg = tSegs[i];
      if (isVarSegment(seg)) {
        // 去掉 {}
        String inside = seg.substring(1, seg.length() - 1).trim();
        int colon = inside.indexOf(':');
        String name, expr = null;
        if (colon >= 0) {
          name = inside.substring(0, colon).trim();
          expr = inside.substring(colon + 1).trim();
        } else {
          name = inside;
        }
        if (name.isEmpty()) {
          throw new IllegalArgumentException("Empty variable in route: " + template);
        }
        varNames[i] = name;
        if (expr != null && !expr.isEmpty()) {
          varPatterns[i] = Pattern.compile(expr);
        }
      } else {
        segLiterals[i] = seg;
        if (firstLiteral == null && !seg.isEmpty()) {
          firstLiteral = seg;
        }
      }
    }

    return new Route(template, handler, segLiterals, varNames, varPatterns, n, firstLiteral);
  }

  /** 分段匹配（与注入） */
  private boolean matchAndInject(Route r, String[] segs, HttpRequest req) {
    if (segs.length != r.segmentsCount)
      return false;

    // 先快速检查首静态段（若存在）
    if (r.firstLiteral != null) {
      if (segs.length == 0 || !r.firstLiteral.equals(segs[0]))
        return false;
    }

    // 逐段匹配
    for (int i = 0; i < segs.length; i++) {
      String lit = r.segLiterals[i];
      if (lit != null) {
        if (!lit.equals(segs[i]))
          return false;
      } else {
        // 变量段：若有约束正则需校验
        Pattern p = r.varPatterns[i];
        if (p != null && !p.matcher(segs[i]).matches())
          return false;
      }
    }

    // 匹配成功后注入变量
    for (int i = 0; i < segs.length; i++) {
      String name = r.varNames[i];
      if (name != null) {
        req.addParam(name, segs[i]);
      }
    }
    return true;
  }

  /** 是否是 {xxx} 段 */
  private static boolean isVarSegment(String seg) {
    return seg.length() >= 2 && seg.charAt(0) == '{' && seg.charAt(seg.length() - 1) == '}';
  }

  /** 快速分割路径/模板（不使用正则），去掉开头的 '/' */
  private static String[] fastSplit(String path) {
    if (path == null || path.isEmpty())
      return new String[0];
    int start = (path.charAt(0) == '/') ? 1 : 0;

    // 统计段数
    int count = 0;
    for (int i = start; i < path.length(); i++) {
      if (path.charAt(i) == '/')
        count++;
    }
    // 段数 = 分隔符数 + 1（若非空）
    int partsCap = (path.length() > start) ? (count + 1) : 0;
    if (partsCap == 0)
      return new String[0];

    List<String> parts = new ArrayList<>(partsCap);
    int segStart = start;
    for (int i = start; i < path.length(); i++) {
      if (path.charAt(i) == '/') {
        if (i > segStart)
          parts.add(path.substring(segStart, i));
        segStart = i + 1;
      }
    }
    if (segStart < path.length()) {
      parts.add(path.substring(segStart));
    }
    return parts.toArray(new String[0]);
  }

  private static String keyOf(int segments, String firstLiteral) {
    return segments + "|" + firstLiteral;
  }
}