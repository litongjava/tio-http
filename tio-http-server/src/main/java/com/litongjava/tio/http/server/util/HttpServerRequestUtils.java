package com.litongjava.tio.http.server.util;

import java.util.Map;

import com.litongjava.tio.http.common.HttpRequest;
import com.litongjava.tio.http.common.Method;
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

  public static Request toOkHttp(String prefix, HttpRequest httpRequest) {

    Builder requestBuilder = new Request.Builder();
    RequestLine requestLine = httpRequest.getRequestLine();
    Method requestMethod = requestLine.getMethod();
    String pathUri = requestLine.getPath();
    requestBuilder.url(prefix + pathUri);

    // 打印请求头信息
    Map<String, String> requestHeaders = httpRequest.getHeaders();
    requestHeaders.remove("host");
    Headers headers = Headers.of(requestHeaders);
    requestBuilder.headers(headers);

    // 设置请求体
    String contentType = httpRequest.getContentType();
    if (contentType != null) {
      if (contentType.startsWith("application/json")) {
        MediaType mediaType = MediaType.parse(contentType);
        @SuppressWarnings("deprecation")
        RequestBody body = RequestBody.create(mediaType, httpRequest.getBodyString());
        requestBuilder.method(requestMethod.toString(), body);

      } else if (contentType.startsWith("application/x-www-form-urlencoded")) {
        FormBody.Builder builder = new FormBody.Builder();
        Map<String, Object[]> params = httpRequest.getParams();
        for (Map.Entry<String, Object[]> e : params.entrySet()) {
          // 添加参数
          builder.add(e.getKey(), (String) e.getValue()[0]);
        }
        requestBuilder.post(builder.build());

      } else if (contentType.startsWith("application/x-www-form-urlencoded")) {
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
      }
    }

    return requestBuilder.build();
  }
}
