package com.litongjava.tio.http.common;

import java.nio.ByteBuffer;

import com.litongjava.tio.core.TioConfig;
import com.litongjava.tio.core.intf.Packet;

@SuppressWarnings("serial")
public abstract class HttpResponsePacket extends Packet {
  public abstract ByteBuffer toByteBuffer(TioConfig tioConfig);
}
