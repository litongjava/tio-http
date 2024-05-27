package com.litongjava.tio.http.server.util;

import com.litongjava.tio.core.ChannelContext;
import com.litongjava.tio.core.Tio;
import com.litongjava.tio.http.common.encoder.ChunkEncoder;
import com.litongjava.tio.http.common.sse.SseBytesPacket;

public class SseUtils {

  public static String LFLF = "\n\n";

  public static void pushChunk(ChannelContext channelContext, String string) {
    String text = "data:" + string + LFLF;
    byte[] bytes = text.getBytes();
    SseBytesPacket ssePacket = new SseBytesPacket(ChunkEncoder.encodeChunk(bytes));
    Tio.send(channelContext, ssePacket);
  }

  public static void pushChunk(ChannelContext channelContext, byte[] bytes) {
    SseBytesPacket ssePacket = new SseBytesPacket(ChunkEncoder.encodeChunk(bytes));
    Tio.send(channelContext, ssePacket);
  }
}
