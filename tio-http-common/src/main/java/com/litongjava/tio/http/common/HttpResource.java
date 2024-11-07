package com.litongjava.tio.http.common;

import java.io.File;
import java.net.URL;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author tanyaowu
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Accessors(chain = true)
public class HttpResource {
  private String path = null;
  private URL url = null;
  private File file = null;

}
