package com.litongjava.tio.http.server.util;

import com.litongjava.tio.core.ChannelContext;
import com.litongjava.tio.core.Tio;
import com.litongjava.tio.http.common.encoder.ChunkEncoder;
import com.litongjava.tio.http.common.sse.ChunkedPacket;

public class SseUtils {

  public static String LFLF = "\n\n";

  public static void pushChunk(ChannelContext channelContext, String string) {
    String text = "data:" + string + LFLF;
    byte[] bytes = text.getBytes();
    ChunkedPacket ssePacket = new ChunkedPacket(ChunkEncoder.encodeChunk(bytes));
    Tio.send(channelContext, ssePacket);
  }

  public static void pushChunk(ChannelContext channelContext, byte[] bytes) {
    ChunkedPacket ssePacket = new ChunkedPacket(ChunkEncoder.encodeChunk(bytes));
    Tio.send(channelContext, ssePacket);
  }

  public static void closeSeeConnection(ChannelContext channelContext) {
    try {
      // 给客户端足够的时间接受消息
      Thread.sleep(1000);
      Tio.remove(channelContext, "remove");
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  public static void closeChunkConnection(ChannelContext channelContext) {
    // 关闭连接
    byte[] zeroChunk = ChunkEncoder.encodeChunk(new byte[0]);
    ChunkedPacket endPacket = new ChunkedPacket(zeroChunk);
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
