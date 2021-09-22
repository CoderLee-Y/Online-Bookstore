package com.Lee.bookstore_backend.chatRoom.config;

import java.util.Map;
import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

@Slf4j
public class HttpHandshakeInterceptor implements HandshakeInterceptor {

  /**
   * 握手前拦截，从 HTTP 中参数传入 WebSocket Attributes 方便后续取出相关参数
   *
   * @param request    请求对象
   * @param response   响应对象
   * @param wsHandler  WebSocket 处理器
   * @param attributes 从 HTTP 握手到与 WebSocket 会话关联的属性
   * @return 如果返回 true 则继续握手，返回 false 则终止握手
   */
  @Override
  public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
      WebSocketHandler wsHandler, Map<String, Object> attributes) {
    // 将 request 对象转换为 ServletServerHttpRequest 对象 并获取
    ServletServerHttpRequest serverRequest = (ServletServerHttpRequest) request;
    HttpSession session = serverRequest.getServletRequest().getSession();

    if (session != null) {
      String username = (String) session.getAttribute("username");
      // 将从 HTTP Session 中获取的用户信息存入 WebSocket 的 Attributes 对象中
      attributes.put("username", username);
      // 继续握手
      return true;
    }
    // 如果没有登录，终止握手
    return false;
  }

  /**
   * 握手完成后调用
   *
   * @param request   请求对象
   * @param response  响应对象
   * @param wsHandler WebSocket 处理器
   * @param ex        异常信息
   */
  @Override
  public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
      WebSocketHandler wsHandler, Exception ex) {
    log.info("完成握手");
  }

}
