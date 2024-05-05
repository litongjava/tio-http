package com.litongjava.tio.http.client;

import com.litongjava.tio.client.intf.ClientAioListener;
import com.litongjava.tio.core.ChannelContext;
import com.litongjava.tio.core.intf.Packet;

/**
 * 
 * @author tanyaowu 
 * 2018年7月8日 上午11:12:15
 */
//TioClientListener
public class HttpTioClientListener implements ClientAioListener {

  public HttpTioClientListener() {
  }

  @Override
  public void onAfterConnected(ChannelContext channelContext, boolean isConnected, boolean isReconnect) {
    return;
  }

  @Override
  public void onAfterDecoded(ChannelContext channelContext, Packet packet, int packetSize) {

  }

  @Override
  public void onAfterSent(ChannelContext channelContext, Packet packet, boolean isSentSuccess) {
    @SuppressWarnings("unused")
    ClientHttpRequest request = (ClientHttpRequest) packet;
  }

  @Override
  public void onBeforeClose(ChannelContext channelContext, Throwable throwable, String remark, boolean isRemove) {

  }

  @Override
  public void onAfterHandled(ChannelContext channelContext, Packet packet, long cost) throws Exception {

  }

  @Override
  public void onAfterReceivedBytes(ChannelContext channelContext, int receivedBytes) throws Exception {

  }
}
