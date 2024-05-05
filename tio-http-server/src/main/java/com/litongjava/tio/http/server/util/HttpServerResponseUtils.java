package com.litongjava.tio.http.server.util;

import com.litongjava.tio.http.common.HttpResponse;
import com.litongjava.tio.http.server.model.HttpCors;

import okhttp3.Response;

public class HttpServerResponseUtils {

  public static void enableCORS(HttpResponse response) {
    CORSUtils.enableCORS(response, new HttpCors());
  }

  public static void enableCORS(HttpResponse response, HttpCors httpCors) {
    CORSUtils.enableCORS(response, httpCors);
  }

  public static void fromOkHttp(Response OkHttpResponse, HttpResponse httpResponse) {
    OkHttpUtils.toTioHttpResponse(OkHttpResponse, httpResponse);
  }

}
