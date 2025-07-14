package com.example.chat_webapp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(@NonNull MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic"); // クライアントにメッセージ送信する宛先のプレフィックス
        config.setApplicationDestinationPrefixes("/app"); // クライアントが送信する宛先のプレフィックス
    }

    @Override
    public void registerStompEndpoints(@NonNull StompEndpointRegistry registry) {
        // エンドポイントのURLを決める。ここにクライアントが接続する
        registry.addEndpoint("/ws-chat").setAllowedOriginPatterns("*").withSockJS();
    }
}
