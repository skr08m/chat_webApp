package com.example.chat_webapp.controller;

import java.time.ZonedDateTime;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import com.example.chat_webapp.dto.ChatMessage;
import com.example.chat_webapp.dto.MessageRequest;
import com.example.chat_webapp.entitiy.ChatMessagesModel;
import com.example.chat_webapp.entitiy.UsersModel;
import com.example.chat_webapp.service.AuthService;
import com.example.chat_webapp.service.ChatService;

@Controller
public class ChatWebSocketController {
    private final AuthService authService;
    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;

    ChatWebSocketController(AuthService authService, ChatService chatService, SimpMessagingTemplate messagingTemplate) {
        this.authService = authService;
        this.chatService = chatService;
        this.messagingTemplate = messagingTemplate;
    }

    /**
     * WebSocket: /app/chat/send/{roomId}
     * リクエストペイロード：{ message }
     * 認証必要
     * 指定ルームにメッセージ送信（WebSocket）→ サーバーが /topic/room.{roomId} へ配信する
     */
    @MessageMapping("/chat/send/{roomId}")
    public void sendMessage(@DestinationVariable String roomId, @Payload MessageRequest content) {
        try {
            // 現在の認証ユーザーを取得
            UsersModel sender = authService.getCurrentUser();

            ChatMessagesModel message = new ChatMessagesModel();
            message.setRoomId(Long.parseLong(roomId));
            message.setSenderId(sender.getUserId());
            message.setContent(content.getMessageRequest());
            message.setCreatedAt(ZonedDateTime.now());
            message.setDeleted(false);

            chatService.saveMessage(message);

            // ブロードキャストするDTO
            ChatMessage response = new ChatMessage();
            response.setSender(message.getSenderId().toString());
            response.setRoomId(message.getRoomId().toString());
            response.setContent(content.getMessageRequest());
            response.setTimestamp(message.getCreatedAt().toString());

            // 特定ルームに送信
            messagingTemplate.convertAndSend("/topic/room." + roomId, response);
        } catch (Exception e) {
            System.out.println("ソケットエラー: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
