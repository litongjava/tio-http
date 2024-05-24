package com.litongjava.tio.http.common.encoder;

import java.nio.charset.StandardCharsets;

public class ChunkEncoder {
  public static byte[] CRLF = "\r\n".getBytes(StandardCharsets.UTF_8);

  public static byte[] encodeChunk(byte[] data) {
    String chunkSize = Integer.toHexString(data.length);
    byte[] chunkSizeBytes = (chunkSize + "\r\n").getBytes(StandardCharsets.UTF_8);

    byte[] chunk = new byte[chunkSizeBytes.length + data.length + CRLF.length];

    System.arraycopy(chunkSizeBytes, 0, chunk, 0, chunkSizeBytes.length);
    System.arraycopy(data, 0, chunk, chunkSizeBytes.length, data.length);
    System.arraycopy(CRLF, 0, chunk, chunkSizeBytes.length + data.length, CRLF.length);

    return chunk;
  }
}
