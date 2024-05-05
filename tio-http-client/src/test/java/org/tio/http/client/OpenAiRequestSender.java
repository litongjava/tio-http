package org.tio.http.client;

import java.io.IOException;

import com.litongjava.tio.client.ClientChannelContext;
import com.litongjava.tio.client.ClientTioConfig;
import com.litongjava.tio.client.ReconnConf;
import com.litongjava.tio.client.TioClient;
import com.litongjava.tio.core.Node;
import com.litongjava.tio.core.Tio;
import com.litongjava.tio.http.client.ClientHttpRequest;
import com.litongjava.tio.http.client.ClientHttpResponse;
import com.litongjava.tio.http.client.HttpTioClientHandler;
import com.litongjava.tio.http.client.HttpTioClientListener;
import com.litongjava.tio.http.common.Method;

public class OpenAiRequestSender {

  public static void main(String[] args) {
    try {
      sendPostRequest();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static void sendPostRequest() throws IOException {
    // Create a new instance of HttpClientStarter
    TioClient tioClient = initializeTioClient();

    // Define the server Node (API endpoint)
    //String serverIp = "api.openai.com";
    //int serverPort = 443; // HTTPS port
    String serverIp = "127.0.0.1";
    int serverPort = 8080; // HTTPS port
    Node serverNode = new Node(serverIp, serverPort);

    // Create a new ClientHttpRequest
    ClientHttpRequest httpRequest = new ClientHttpRequest(Method.POST, "/v1/chat/completions", null);
    httpRequest.addHeader("Accept", "*/*");
    httpRequest.addHeader("Content-Type", "application/json");

    // Define the JSON body as a String
    String jsonBody = "{\n" + "    \"messages\": [\n" + "        {\n" + "            \"role\": \"system\",\n"
        + "            \"content\": \"Just say hi\"\n" + "        },\n" + "        {\n"
        + "            \"role\": \"user\",\n" + "            \"content\": \"create a story about a boy with dog\"\n"
        + "        }\n" + "    ],\n" + "    \"model\": \"gpt-3.5-turbo\"\n" + "}";

    // Set the body of the request
    httpRequest.setBody(jsonBody.getBytes("UTF-8"));

    // Connect to the server
    ClientChannelContext channelContext;
    try {
      channelContext = tioClient.connect(serverNode);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

    // Send the request
    Tio.send(channelContext, httpRequest);

    // Handle the response (this could be extended based on what you need)
    ClientHttpResponse response = (ClientHttpResponse) channelContext.getAttribute(HttpTioClientHandler.RESPONSE_KEY);
    if (response != null) {
      System.out.println("Response: " + response.getBodyString());
    } else {
      System.out.println("No response received.");
    }
  }

  private static TioClient initializeTioClient() throws IOException {
    // 创建一个处理器和监听器
    HttpTioClientHandler clientHandler = new HttpTioClientHandler();
    HttpTioClientListener clientListener = new HttpTioClientListener();

    // 重连配置
    ReconnConf reconnConf = new ReconnConf(5000L); // 5000 ms

    // 创建客户端配置对象
    ClientTioConfig tioClientConfig = new ClientTioConfig(clientHandler, clientListener, reconnConf);
    tioClientConfig.setName("Tio Http Client");
    tioClientConfig.setHeartbeatTimeout(60000); // 心跳超时时间

    // 启用 SSL
//    try {
//      tioClientConfig.useSsl();
//    } catch (Exception e) {
//      e.printStackTrace();
//    }

    // 创建 TioClient
    return new TioClient(tioClientConfig);
  }
}
