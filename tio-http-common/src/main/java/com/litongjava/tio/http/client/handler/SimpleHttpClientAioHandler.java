package com.litongjava.tio.http.client.handler;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

import com.litongjava.aio.Packet;
import com.litongjava.tio.client.intf.ClientAioHandler;
import com.litongjava.tio.core.ChannelContext;
import com.litongjava.tio.core.TioConfig;
import com.litongjava.tio.http.client.packet.ChunkParseResult;
import com.litongjava.tio.http.client.packet.HttpRequestPacket;
import com.litongjava.tio.http.client.packet.HttpResponsePacket;

public class SimpleHttpClientAioHandler implements ClientAioHandler {

  @Override
  public ByteBuffer encode(Packet packet, TioConfig tioConfig, ChannelContext channelContext) {
    if (!(packet instanceof HttpRequestPacket)) {
      throw new IllegalArgumentException("unsupported packet: " + packet);
    }
    byte[] bytes = ((HttpRequestPacket) packet).getBytes();
    return ByteBuffer.wrap(bytes);
  }

  @Override
  public Packet decode(ByteBuffer buffer, int limit, int position, int readableLength, ChannelContext channelContext)
      throws Exception {

    // 需要：从 buffer[position..position+readableLength) 中解析一个完整 HTTP 响应
    // 解析策略：先找 \r\n\r\n，得到 headers，然后根据 Content-Length 或 chunked 读 body。
    int start = position;
    int end = position + readableLength;

    int headerEnd = indexOf(buffer, start, end, CRLFCRLF);
    if (headerEnd < 0) {
      return null; // 还没收到完整 headers
    }

    int headersBytesEnd = headerEnd + 4;
    byte[] headerBytes = new byte[headersBytesEnd - start];
    for (int i = 0; i < headerBytes.length; i++) {
      headerBytes[i] = buffer.get(start + i);
    }
    String headerText = new String(headerBytes, StandardCharsets.ISO_8859_1);
    String[] lines = headerText.split("\r\n");
    if (lines.length == 0)
      return null;

    HttpResponsePacket resp = new HttpResponsePacket();
    resp.statusLine = lines[0];

    // status code
    String[] parts = lines[0].split(" ");
    if (parts.length >= 2) {
      try {
        resp.statusCode = Integer.parseInt(parts[1]);
      } catch (Exception ignore) {
      }
    }

    boolean chunked = false;
    int contentLength = -1;

    for (int i = 1; i < lines.length; i++) {
      String line = lines[i];
      int idx = line.indexOf(':');
      if (idx <= 0)
        continue;
      String k = line.substring(0, idx).trim();
      String v = line.substring(idx + 1).trim();
      resp.headers.put(k, v);

      String kl = k.toLowerCase(Locale.ROOT);
      if (kl.equals("content-length")) {
        try {
          contentLength = Integer.parseInt(v.trim());
        } catch (Exception ignore) {
        }
      } else if (kl.equals("transfer-encoding") && v.toLowerCase(Locale.ROOT).contains("chunked")) {
        chunked = true;
      }
    }

    int bodyStart = headersBytesEnd;

    if (chunked) {
      // 解析 chunked：如果不完整，返回 null
      ChunkParseResult cpr = parseChunkedBody(buffer, bodyStart, end);
      if (cpr == null)
        return null;
      resp.body = cpr.body;

      // 消费长度 = headers + chunked 总长度
      buffer.position(start + (headersBytesEnd - start) + cpr.consumedBytes);
      return resp;
    } else {
      if (contentLength < 0) {
        // 没有 length 也没 chunked：这里简单处理为“等待对端关闭”不实现（测试代理通常够用）
        // 也可以扩展：若 Connection: close，则读到 end 即 body
        int bodyLen = end - bodyStart;
        byte[] body = new byte[bodyLen];
        for (int i = 0; i < bodyLen; i++)
          body[i] = buffer.get(bodyStart + i);
        resp.body = body;
        buffer.position(end);
        return resp;
      }

      int needEnd = bodyStart + contentLength;
      if (needEnd > end) {
        return null; // body 还没收全
      }

      byte[] body = new byte[contentLength];
      for (int i = 0; i < contentLength; i++) {
        body[i] = buffer.get(bodyStart + i);
      }
      resp.body = body;

      buffer.position(needEnd);
      return resp;
    }
  }

  @Override
  public Packet heartbeatPacket(ChannelContext channelContext) {
    return null;
  }

  private static final byte[] CRLFCRLF = new byte[] { '\r', '\n', '\r', '\n' };

  private static int indexOf(ByteBuffer buf, int start, int end, byte[] pat) {
    outer: for (int i = start; i <= end - pat.length; i++) {
      for (int j = 0; j < pat.length; j++) {
        if (buf.get(i + j) != pat[j])
          continue outer;
      }
      return i;
    }
    return -1;
  }

  private static ChunkParseResult parseChunkedBody(ByteBuffer buf, int bodyStart, int end) {
    int p = bodyStart;
    java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();

    while (true) {
      int lineEnd = indexOf(buf, p, end, new byte[] { '\r', '\n' });
      if (lineEnd < 0)
        return null;
      String hex = readAscii(buf, p, lineEnd).trim();
      int semi = hex.indexOf(';');
      if (semi >= 0)
        hex = hex.substring(0, semi).trim();

      int size;
      try {
        size = Integer.parseInt(hex, 16);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
      p = lineEnd + 2;

      if (size == 0) {
        // 终止块后还有一行空行 \r\n（以及可能的 trailer，这里忽略 trailer，找 \r\n 结束）
        int trailerEnd = indexOf(buf, p, end, CRLFCRLF);
        if (trailerEnd < 0) {
          // 有些实现只有 \r\n
          int simple = indexOf(buf, p, end, new byte[] { '\r', '\n' });
          if (simple < 0)
            return null;
          p = simple + 2;
        } else {
          p = trailerEnd + 4;
        }
        ChunkParseResult r = new ChunkParseResult();
        r.body = baos.toByteArray();
        r.consumedBytes = p - bodyStart;
        return r;
      }

      if (p + size + 2 > end)
        return null;
      for (int i = 0; i < size; i++)
        baos.write(buf.get(p + i));
      p += size;

      // 读 chunk 末尾 CRLF
      if (p + 2 > end)
        return null;
      if (buf.get(p) != '\r' || buf.get(p + 1) != '\n')
        throw new RuntimeException("invalid chunk ending");
      p += 2;
    }
  }

  private static String readAscii(ByteBuffer buf, int s, int e) {
    byte[] b = new byte[e - s];
    for (int i = 0; i < b.length; i++)
      b[i] = buf.get(s + i);
    return new String(b, StandardCharsets.US_ASCII);
  }

  @Override
  public void handler(Packet packet, ChannelContext channelContext) throws Exception {

  }

}
