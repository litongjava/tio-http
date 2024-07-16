package com.litongjava.tio.http.client;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class ClientHttpRequest {
  private String method;
  private URI url;
  private Map<String, String> headers = new HashMap<>();
  private String body;

  public ClientHttpRequest(String method, URI url) {
    this.method = method;
    this.url = url;
  }

  public ClientHttpRequest(String method, String url) {
    this.method = method;
    try {
      this.url = new URI(url);
    } catch (URISyntaxException e) {
      throw new RuntimeException(e);
    }
  }

  public ClientHttpRequest setHeader(String key, String value) {
    headers.put(key, value);
    return this;
  }
}
