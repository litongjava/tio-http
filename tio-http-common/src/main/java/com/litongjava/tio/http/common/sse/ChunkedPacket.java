package com.litongjava.tio.http.common.sse;

import java.nio.ByteBuffer;

import com.litongjava.tio.core.TioConfig;
import com.litongjava.tio.http.common.HttpResponsePacket;

public class ChunkedPacket extends HttpResponsePacket {
  private static final long serialVersionUID = 1014364783783749718L;
  private byte[] bytes;

  public ChunkedPacket() {
    super();
  }

  @Override
  public ByteBuffer toByteBuffer(TioConfig tioConfig) {
    ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
    buffer.order(tioConfig.getByteOrder());
    buffer.put(bytes);
    buffer.flip();
    return buffer;
  }

  public ChunkedPacket(byte[] bytes) {
    this.bytes = bytes;
  }

  public ChunkedPacket(String line) {
    this.bytes = line.getBytes();
  }

  public byte[] getBytes() {
    return bytes;
  }

  public void setBytes(byte[] bytes) {
    this.bytes = bytes;
  }
}