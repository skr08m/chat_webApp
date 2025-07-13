package com.example.chat_webapp.controller;

import org.springframework.messaging.handler.annotation.*;
import org.springframework.stereotype.Controller;

@Controller
public class ChatWebSocketController {

    /**
     * WebSocket: /app/chat/send/{roomId}
     * リクエストペイロード：{ message }
     * 認証必要
     * 指定ルームにメッセージ送信（WebSocket）→ サーバーが /topic/room.{roomId} へ配信する
     */
    @MessageMapping("/chat/send/{roomId}")
    @SendTo("/topic/room.{roomId}")
    public ChatMessage sendMessage(@DestinationVariable String roomId, ChatMessage message) {
        return message; // 仮の実装
    }
}

