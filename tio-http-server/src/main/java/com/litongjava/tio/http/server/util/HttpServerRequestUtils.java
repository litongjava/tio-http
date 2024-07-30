package com.litongjava.tio.http.server.util;

import java.util.Map;

import com.litongjava.tio.http.common.HttpMethod;
import com.litongjava.tio.http.common.HttpRequest;
import com.litongjava.tio.http.common.RequestLine;
import com.litongjava.tio.http.common.UploadFile;

import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.RequestBody;

public class HttpServerRequestUtils {

  public static Request buildOkHttpRequest(String prefix, HttpRequest httpRequest) {
    RequestLine requestLine = httpRequest.getRequestLine();
    HttpMethod requestMethod = requestLine.getMethod();
    String pathAndQuery = requestLine.getPathAndQuery();
    String targetUrl = prefix + pathAndQuery;

    Builder requestBuilder = new Request.Builder();
    requestBuilder.url(targetUrl);

    // 打印请求头信息
    Map<String, String> requestHeaders = httpRequest.getHeaders();
    Headers headers = Headers.of(requestHeaders);
    requestBuilder.headers(headers);

    // 设置请求体
    if (HttpMethod.POST.equals(requestMethod)) {
      String contentType = httpRequest.getContentType();
      if (contentType != null) {
        if (contentType.startsWith("application/json")) {
          MediaType mediaType = MediaType.parse(contentType);
          @SuppressWarnings("deprecation")

          RequestBody reqeustBody = RequestBody.create(mediaType, httpRequest.getBodyString());

          requestBuilder.method(requestMethod.toString(), reqeustBody);

        } else if (contentType.startsWith("application/x-www-form-urlencoded")) {
          FormBody.Builder builder = new FormBody.Builder();
          Map<String, Object[]> params = httpRequest.getParams();
          for (Map.Entry<String, Object[]> e : params.entrySet()) {
            // 添加参数
            builder.add(e.getKey(), (String) e.getValue()[0]);
          }
          requestBuilder.post(builder.build());

        } else if (contentType.startsWith("multipart/form-data")) {
          MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
          Map<String, Object[]> params = httpRequest.getParams();
          for (Map.Entry<String, Object[]> e : params.entrySet()) {
            Object value = e.getValue()[0];
            // 添加参数
            if (value instanceof String) {
              builder.addFormDataPart(e.getKey(), (String) value);
            } else {
              UploadFile uploadFile = httpRequest.getUploadFile(e.getKey());
              RequestBody fileBody = RequestBody.create(uploadFile.getData());
              builder.addFormDataPart(e.getKey(), uploadFile.getName(), fileBody);
            }
          }
          requestBuilder.post(builder.build());
        } else {
          MediaType mediaType = MediaType.parse(contentType);
          @SuppressWarnings("deprecation")
          RequestBody body = RequestBody.create(mediaType, httpRequest.getBodyString());
          requestBuilder.post(body);
        }
      } else {
        MediaType mediaType = MediaType.parse("text/plain");
        @SuppressWarnings("deprecation")
        RequestBody body = RequestBody.create(mediaType, "");
        requestBuilder.post(body);
      }
    } else if (HttpMethod.GET.equals(requestMethod)) {
      requestBuilder.get();
    }

    return requestBuilder.build();
  }
}
