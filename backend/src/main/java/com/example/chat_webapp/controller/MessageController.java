package com.example.chat_webapp.controller;

import com.example.chat_webapp.service.ChatService;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.chat_webapp.entitiy.ChatMessagesModel;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final ChatService chatService;

    MessageController(ChatService chatService) {
        this.chatService = chatService;
    }

    /**
     * GET /api/messages/{roomId}
     * リクエストボディ：なし
     * 認証必要
     * 指定ルームのメッセージ一覧を取得する
     */
    @GetMapping("/{roomId}")
    public ResponseEntity<?> getMessages(@PathVariable Long roomId) {
        List<ChatMessagesModel> messages = chatService.getMessagesByRoomId(roomId);
        return ResponseEntity.ok(messages);
    }
}
