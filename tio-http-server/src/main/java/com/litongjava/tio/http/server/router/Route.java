package com.litongjava.tio.http.server.router;

import java.util.List;
import java.util.regex.Pattern;

import com.litongjava.tio.http.server.handler.HttpRequestHandler;

public class Route {
  final String template;
  final Pattern pattern;
  final List<String> varNames;
  final HttpRequestHandler handler;

  Route(String template, Pattern pattern, List<String> varNames, HttpRequestHandler handler) {
    this.template = template;
    this.pattern = pattern;
    this.varNames = varNames;
    this.handler = handler;
  }
}