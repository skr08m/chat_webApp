package com.example.chat_webapp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    // WebSocketハンドシェイク時にJWTトークンを検証するインターセプター
    @Autowired
    private JwtHandshakeInterceptor jwtHandshakeInterceptor;

    @Override
    public void configureMessageBroker(@NonNull MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic"); // クライアントにメッセージ送信する宛先のプレフィックス
        config.setApplicationDestinationPrefixes("/app"); // クライアントが送信する宛先のプレフィックス
    }

    @Override
    public void registerStompEndpoints(@NonNull StompEndpointRegistry registry) {
        // エンドポイントのURLを決める。ここにクライアントが接続する
        registry.addEndpoint("/ws-chat")
                .addInterceptors(jwtHandshakeInterceptor) // JWT認証インターセプターを追加
                .setAllowedOriginPatterns("*")
                .withSockJS(); // SockJSを有効にすることで、WebSocket非対応環境にも対応
    }
}
