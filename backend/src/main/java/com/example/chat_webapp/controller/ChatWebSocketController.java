package com.example.chat_webapp.controller;

import java.security.Principal;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.example.chat_webapp.dto.ChatMessage;
import com.example.chat_webapp.dto.MessageRequest;
import com.example.chat_webapp.entitiy.ChatMessagesModel;
import com.example.chat_webapp.entitiy.UsersModel;
import com.example.chat_webapp.repository.UserRepository;
import com.example.chat_webapp.service.ChatService;

@Controller
public class ChatWebSocketController {
    private final UserRepository userRepository;// ほんとはよくない
    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;

    ChatWebSocketController(UserRepository userRepository, ChatService chatService,
            SimpMessagingTemplate messagingTemplate) {
        this.userRepository = userRepository;
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
    public void sendMessage(@DestinationVariable String roomId, @Payload MessageRequest content, Principal principal) {
        try {
            String email = principal.getName(); // JWTの `sub` が入る（email）
            // 現在の認証ユーザーを取得
            UsersModel sender = userRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            ChatMessagesModel message = new ChatMessagesModel();
            message.setRoomId(Long.parseLong(roomId));
            message.setSenderId(sender.getUserId());
            message.setContent(content.getMessageRequest());
            message.setCreatedAt(ZonedDateTime.now());
            message.setDeleted(false);

            Long messageId =  chatService.saveMessage(message).getId();

            // ブロードキャストするDTO
            ChatMessage response = new ChatMessage();
            response.setId(messageId);
            response.setSenderId(message.getSenderId().toString());
            response.setRoomId(message.getRoomId().toString());
            response.setContent(content.getMessageRequest());
            response.setTimestamp(message.getCreatedAt().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));

            // 特定ルームに送信
            messagingTemplate.convertAndSend("/topic/room." + roomId, response);
        } catch (Exception e) {
            System.out.println("ソケットエラー: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
