package com.litongjava.tio.http.common;

/**
 * UploadFile
 */
public class UploadFile {

  private String name = null;
  private long size = -1;
  private byte[] data = null;

  public UploadFile() {
  }

  public UploadFile(String filename, byte[] bytes) {
    this.name = filename;
    this.data = bytes;
  }

  public UploadFile(String name, long size, byte[] data) {
    super();
    this.name = name;
    this.size = size;
    this.data = data;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public long getSize() {
    return size;
  }

  public void setSize(long size) {
    this.size = size;
  }

  public byte[] getData() {
    return data;
  }

  public void setData(byte[] data) {
    this.data = data;
  }

}
