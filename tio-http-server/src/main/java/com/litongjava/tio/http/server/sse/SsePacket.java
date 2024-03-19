package com.litongjava.tio.http.server.sse;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import com.litongjava.tio.core.TioConfig;
import com.litongjava.tio.core.intf.Packet;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SsePacket extends Packet {
  private static final long serialVersionUID = 1L;
  private Charset charset = StandardCharsets.UTF_8;
  private byte[] nBytes = "\n".getBytes(charset);

  private String event;
  private byte[] data;

  public SsePacket id(int i) {
    super.setId((long) i);
    return this;
  }

  public SsePacket id(Long id) {
    super.setId(id);
    return this;
  }

  public SsePacket event(String event) {
    this.event = event;
    return this;
  }

  public SsePacket data(byte[] data) {
    this.data = data;
    return this;
  }

  public ByteBuffer toByteBuffer(TioConfig tioConfig) {
    ByteBuffer buffer = ByteBuffer.allocate(calculateBufferSize());
    buffer.order(tioConfig.getByteOrder());

    // Add id
    if (getId() != null) {
      String idString = "id:" + getId() + "\n";
      buffer.put(idString.getBytes(charset));
    }

    // Add event
    if (event != null) {
      String eventString = "event:" + event + "\n";
      buffer.put(eventString.getBytes(charset));
    }

    // Add data
    if (data != null) {
      buffer.put("data:".getBytes(charset));
      buffer.put(data);
      buffer.put(nBytes);

    }

    buffer.put(nBytes);
    buffer.flip();
    return buffer;
  }

  private int calculateBufferSize() {
    int size = 0;

    // id
    if (getId() != null) {
      size += ("id:" + getId()).getBytes(charset).length + nBytes.length;
    }

    // event
    if (event != null) {
      size += ("event:" + event).getBytes(charset).length + nBytes.length;
    }

    // data
    if (data != null) {
      size += ("data:").getBytes(charset).length + nBytes.length + data.length;
    }

    // Add extra newline
    size += nBytes.length;

    return size;
  }

}