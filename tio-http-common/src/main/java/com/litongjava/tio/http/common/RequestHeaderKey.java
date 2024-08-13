package com.litongjava.tio.http.common;

public interface RequestHeaderKey {
  String Cookie = "cookie"; // .toLowerCase();//Cookie: $Version=1; Skin=new;
  String Origin = "origin"; // .toLowerCase(); //http://127.0.0.1
  String Sec_WebSocket_Key = "sec-websocket-key"; // .toLowerCase(); //2GFwqJ1Z37glm62YKKLUeA==
  String Cache_Control = "cache-control"; // .toLowerCase(); //"public, max-age:86400"
  String Connection = "connection"; // .toLowerCase(); //Upgrade, keep-alive
  String User_Agent = "user-agent"; // .toLowerCase(); //Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3088.3 Safari/537.36
  String Sec_WebSocket_Version = "sec-websocket-version"; // .toLowerCase(); //13
  String Host = "host"; // .toLowerCase(); //127.0.0.1:9321
  String Pragma = "pragma"; // .toLowerCase(); //no-cache
  String Accept_Encoding = "accept-encoding"; // .toLowerCase(); //gzip, deflate, br
  String Accept_Language = "accept-language"; // .toLowerCase(); //zh-CN,zh;q=0.8,en;q=0.6
  String Upgrade = "upgrade"; // .toLowerCase(); //websocket
  String Sec_WebSocket_Extensions = "sec-websocket-extensions"; // .toLowerCase(); //permessage-deflate; client_max_window_bits
  String Content_Length = "content-length"; // .toLowerCase(); //65
  String Content_Type = "content-type"; // .toLowerCase();// : 【application/x-www-form-urlencoded】【application/x-www-form-urlencoded; charset=UTF-8】【multipart/form-data; boundary=----WebKitFormBoundaryuwYcfA2AIgxqIxA0 】
  String If_Modified_Since = "if-modified-since"; // .toLowerCase(); //与Last-Modified配合

  String Referer = "referer";

  /**
   * 值为XMLHttpRequest则为Ajax
   */
  String X_Requested_With = "x-requested-with";// .toLowerCase();//XMLHttpRequest
  String X_forwarded_For = "x-forwarded-for";
}