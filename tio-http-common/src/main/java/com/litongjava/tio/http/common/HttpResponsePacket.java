package com.litongjava.tio.http.common;

import java.nio.ByteBuffer;

import com.litongjava.aio.Packet;
import com.litongjava.tio.core.TioConfig;

@SuppressWarnings("serial")
public abstract class HttpResponsePacket extends Packet {
  public abstract ByteBuffer toByteBuffer(TioConfig tioConfig);
}
