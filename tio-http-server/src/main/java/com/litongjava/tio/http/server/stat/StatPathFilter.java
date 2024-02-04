package com.litongjava.tio.http.server.stat;

import com.litongjava.tio.http.common.HttpRequest;
import com.litongjava.tio.http.common.HttpResponse;

/**
 * @author tanyaowu
 *
 */
public interface StatPathFilter {

  /**
   * 
   * @param path
   * @param request
   * @param response
   * @return true: 表示要统计， false: 不统计
   */
  public boolean filter(String path, HttpRequest request, HttpResponse response);
}
