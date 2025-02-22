package com.litongjava.tio.http.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * UploadFile
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class UploadFile {

  private String name = null;
  private long size = -1;
  private byte[] data = null;

  public UploadFile(String filename, byte[] bytes) {
    this.name = filename;
    this.data = bytes;
  }
}
