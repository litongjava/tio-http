package com.litongjava.tio.http.common.utils;

import com.litongjava.tio.http.common.HeaderName;
import com.litongjava.tio.http.common.HeaderValue;
import com.litongjava.tio.http.common.HttpRequest;
import com.litongjava.tio.http.common.HttpResponse;
import com.litongjava.tio.http.common.RequestHeaderKey;
import com.litongjava.tio.utils.hutool.ZipUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * @author tanyaowu
 * 2017年8月18日 下午5:47:00
 */
@Slf4j
public class HttpGzipUtils {
  /**
   * 
   * @param request
   * @param response
   * @author tanyaowu
   */
  public static void gzip(HttpRequest request, HttpResponse response) {
    if (response == null) {
      return;
    }

    // 已经gzip过了，就不必再压缩了
    if (response.hasGzipped()) {
      return;
    }

    if (request != null && request.getIsSupportGzip()) {
      justGzip(response);
    } else {
      if (request != null) {
        log.warn("not support gzip:{}, {}", request.getClientIp(), request.getHeader(RequestHeaderKey.User_Agent));
      }
    }
  }

  /**
   * 
   * @param response
   * @author tanyaowu
   */
  public static void gzip(HttpResponse response) {
    if (response == null) {
      return;
    }

    // 已经gzip过了，就不必再压缩了
    if (response.hasGzipped()) {
      return;
    }

    justGzip(response);
  }

  public static void justGzip(HttpResponse response) {
    byte[] bs = response.getBody();
    if (bs != null && bs.length >= 300) {
      byte[] bs2 = ZipUtil.gzip(bs);
      if (bs2.length < bs.length) {
        response.setBody(bs2);
        response.setHasGzipped(true);
        response.addHeader(HeaderName.Content_Encoding, HeaderValue.Content_Encoding.gzip);
      }
    }
  }
}
