package com.litongjava.tio.http.common.sse;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import com.litongjava.tio.core.TioConfig;
import com.litongjava.tio.http.common.HttpResponsePacket;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SsePacket extends HttpResponsePacket {
  private static final byte[] CRLF = { 13, 10 };

  private static final byte[] idBytes = { 105, 100, 58 };
  private static final byte[] eventBytes = { 101, 118, 101, 110, 116, 58 };
  private static final byte[] dataBytes = { 100, 97, 116, 97, 58 };

  private static final long serialVersionUID = 1L;
  private Charset charset = StandardCharsets.UTF_8;

  private Long eventId;
  private String event;
  private byte[] data;

  public SsePacket(Long id, String event, byte[] data) {
    this.eventId = id;
    this.event = event;
    this.data = data;
  }

  public SsePacket(String event, byte[] data) {
    this.event = event;
    this.data = data;
  }

  public SsePacket(String event, String data) {
    this.event = event;
    this.data = data.getBytes();
  }

  public SsePacket(byte[] data) {
    this.data = data;
  }

  public SsePacket id(int i) {
    eventId = ((long) i);
    return this;
  }

  public SsePacket id(Long id) {
    eventId = (id);
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

  @Override
  public ByteBuffer toByteBuffer(TioConfig tioConfig) {
    ByteBuffer buffer = ByteBuffer.allocate(calculateBufferSize());
    buffer.order(tioConfig.getByteOrder());

    // Add id
    if (eventId != null) {
      buffer.put(idBytes);
      buffer.put(eventId.toString().getBytes(charset)).put(CRLF);
    }

    // Add event
    if (event != null) {
      buffer.put(eventBytes).put(event.getBytes(charset)).put(CRLF);
    }

    // Add data
    if (data != null) {
      buffer.put(dataBytes);
      buffer.put(data);
      buffer.put(CRLF);

    }

    buffer.put(CRLF);
    buffer.flip();
    return buffer;
  }

  private int calculateBufferSize() {
    int size = 0;

    // id
    if (eventId != null) {
      size += 3 + eventId.toString().getBytes(charset).length + 2;
    }

    // event
    if (event != null) {
      size += 6 + event.toString().getBytes(charset).length + 2;
    }

    // data
    if (data != null) {
      size += 5 + 2 + data.length;
    }

    // Add extra newline
    size += CRLF.length;

    return size;
  }
}