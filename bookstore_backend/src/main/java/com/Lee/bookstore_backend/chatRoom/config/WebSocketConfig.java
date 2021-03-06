package com.Lee.bookstore_backend.chatRoom.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /**
     * 配置消息代理选项
     *
     * @param registry 消息代理注册配置
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
//        Websocket前缀
        // 设置一个或者多个代理前缀，在 Controller 类中的方法里面发生的消息，会首先转发到代理从而发送到对应广播或者队列中。
//        订阅地址会是/websocket/service/xxx(我准备使用书本ID表示本书的客服
        registry.enableSimpleBroker("/websocket/service");
        // 配置客户端发送请求消息的一个或多个前缀，该前缀会筛选消息目标转发到 Controller 类中注解对应的方法里
        registry.setApplicationDestinationPrefixes("/app");
    }

    /**
     * 配置 WebSocket 进入点，及开启使用 SockJS，这些配置主要用配置连接端点，用于 WebSocket 连接
     *
     * @param registry STOMP Endpoint
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 配置 websocket 进入点
        registry.addEndpoint("/iBookStore")
                .setAllowedOrigins("http://localhost:3000")
//            握手拦截器，对端点
                .addInterceptors(new HttpHandshakeInterceptor())
//            握手处理器，对端点
                .setHandshakeHandler(new HttpHandshakeHandler())
                .withSockJS();
    }

    /**
     * 与处理从 WebSocket 客户端接收和发送到 WebSocket 的消息相关的配置选项。
     *
     * @param registry STOMP 端点
     */
    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registry) {
        registry.addDecoratorFactory(new HttpWebSocketHandlerDecoratorFactory());
    }

}
