package com.litongjava.tio.http.client;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class ClientHttpResponse {
  private int statusCode;
  private Map<String, String> headers;
  private byte[] body;

  public ClientHttpResponse(int statusCode, byte[] body) {
    this.statusCode = statusCode;
    this.body = body;
  }
}