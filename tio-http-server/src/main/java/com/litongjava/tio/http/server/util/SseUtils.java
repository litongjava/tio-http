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

  public static void closeSeeConnection(ChannelContext channelContext) {

  }

  public static void closeChunkConnection(ChannelContext channelContext) {
    // 关闭连接
    byte[] zeroChunk = ChunkEncoder.encodeChunk(new byte[0]);
    SseBytesPacket endPacket = new SseBytesPacket(zeroChunk);
    Tio.send(channelContext, endPacket);

    try {
      // 给客户端足够的时间接受消息
      Thread.sleep(1000);
      Tio.remove(channelContext, "remove");
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

}
