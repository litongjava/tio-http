package com.litongjava.tio.http.client;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.litongjava.tio.client.intf.ClientAioHandler;
import com.litongjava.tio.core.ChannelContext;
import com.litongjava.tio.core.TioConfig;
import com.litongjava.tio.core.exception.TioDecodeException;
import com.litongjava.tio.core.intf.Packet;
import com.litongjava.tio.utils.SysConst;

/**
 * 
 * @author tanyaowu 
 * 2018年7月8日 上午11:12:21
 */
//
//TioClientHandler
public class HttpTioClientHandler implements ClientAioHandler {
  private static Logger log = LoggerFactory.getLogger(HttpTioClientHandler.class);

  public static final String RESPONSE_KEY = "tio_response_key";

  /**
   * @author tanyaowu
   * 2016年11月18日 上午9:13:15
   *
   */
  public HttpTioClientHandler() {
  }

  @Override
  public ClientHttpResponse decode(ByteBuffer buffer, int limit, int position, int readableLength,
      ChannelContext channelContext) throws TioDecodeException {
    ClientHttpResponse response = HttpResponseDecoder.decode(buffer, limit, position, readableLength, channelContext);
    channelContext.setAttribute(RESPONSE_KEY, response);
    return response;
  }

  @Override
  public ByteBuffer encode(Packet packet, TioConfig tioConfig, ChannelContext channelContext) {
    ClientHttpRequest request = (ClientHttpRequest) packet;
    ByteBuffer byteBuffer;
    try {
      byteBuffer = HttpRequestEncoder.encode(request, tioConfig, channelContext);
      return byteBuffer;
    } catch (UnsupportedEncodingException e) {
      log.error(e.toString(), e);
      return null;
    }
  }

  @Override
  public void handler(Packet packet, ChannelContext channelContext) throws Exception {
    // ClientHttpResponse response = (ClientHttpResponse) packet;
    long c = HttpClientStarter.receivedCount.incrementAndGet();
    long stageC = HttpClientStarter.receivedStageCount.incrementAndGet();

    long bs = HttpClientStarter.receivedBytes.addAndGet(packet.getByteCount());
    long stageBs = HttpClientStarter.receivedStageBytes.addAndGet(packet.getByteCount());

    if (c % HttpClientStarter.stepCount == 0) {
      long endtime = System.currentTimeMillis();
      long stageIv = endtime - HttpClientStarter.stageStartTime;
      long iv = endtime - HttpClientStarter.startTime;
      // System.out.println("已经完成请求数：" + StrUtil.fillBefore(c + "", '0', 10) + ", 本次耗时：" + stageIv + "ms, 总耗时：" + iv + "ms, " + "本次平均每秒处理请求数：" + (1000 * (stageC / stageIv)));

      /*
       * %10s: 输出固定长度为10的字符串 默认右对齐 %-10s: 输出固定长度10的字符串 左对齐;
       */
      System.out.printf("已收到响应数：%-12s 流量：%-12s 本次耗时：%-8s 总耗时：%-8s " + "本次R/S：%-10s  本次吞吐量/S：%-10s\r\n", c,
          (bs / 1024) + " KB", stageIv + " ms", iv + " ms", (1000 * (stageC / stageIv)),
          (1000 * (stageBs / stageIv)) / 1024 + " KB");

      HttpClientStarter.stageStartTime = System.currentTimeMillis();
      HttpClientStarter.receivedStageCount.set(0);
      HttpClientStarter.receivedStageBytes.set(0);

    }
    if (c == HttpClientStarter.totalRequestCount) {
      long endtime = System.currentTimeMillis();
      long iv = endtime - HttpClientStarter.startTime;
      System.out.printf(SysConst.CRLF);
      System.out.printf("%-30s%-20s\r\n", "request path", HttpClientStarter.requestPath);
      System.out.printf("%-30s%-20s\r\n", "client count", HttpClientStarter.clientCount);
      System.out.printf("%-30s%-20s\r\n", "complete requests", c);
      System.out.printf("%-30s%-20s\r\n", "time taken for tests", iv + " ms");
      System.out.printf("%-30s%-20s\r\n", "requests per second", (1000 * (c / iv)));
      System.out.printf("%-30s%-20s\r\n", "transfer rate(KB/S)", ((1000 * (bs / iv)) / 1024));
      System.out.printf("%-30s%-20s\r\n", "Bytes/Response", bs / c);
      System.out.printf(SysConst.CRLF);
    }

  }

  /** 
   * @return
   * @author tanyaowu
   */
  @Override
  public Packet heartbeatPacket(ChannelContext channelContext) {
    return null;
  }

}
