package com.litongjava.tio.http.server.router;

import java.util.regex.Pattern;

import com.litongjava.tio.http.server.handler.HttpRequestHandler;

/** 
 * 模板路由结构：分段匹配，变量段可带约束正则 
 * */
public class Route {
  final String template;
  final HttpRequestHandler handler;

  final String[] segLiterals; // 非变量段的字面值；变量段为 null
  final String[] varNames; // 变量名；非变量段为 null
  final Pattern[] varPatterns; // 变量段正则；未约束为 null
  final int segmentsCount;
  final String firstLiteral; // 首个静态段（可能为 null）

  Route(String template, HttpRequestHandler handler, String[] segLiterals, String[] varNames, Pattern[] varPatterns,
      int segmentsCount, String firstLiteral) {
    this.template = template;
    this.handler = handler;
    this.segLiterals = segLiterals;
    this.varNames = varNames;
    this.varPatterns = varPatterns;
    this.segmentsCount = segmentsCount;
    this.firstLiteral = firstLiteral;
  }
}