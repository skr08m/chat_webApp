package com.example.chat_webapp.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    /**
     * GET /api/messages/{roomId}
     * リクエストボディ：なし
     * 認証必要
     * 指定ルームのメッセージ一覧を取得する
     */
    @GetMapping("/{roomId}")
    public ResponseEntity<?> getMessages(@PathVariable Long roomId) {
        return ResponseEntity.ok().build();
    }

    /**
     * POST /api/messages/{roomId}
     * リクエストボディ：{ message }
     * 認証必要
     * 指定ルームにメッセージを送信（WebSocketとは別で履歴保存用）
     */
    @PostMapping("/{roomId}")
    public ResponseEntity<?> postMessage(@PathVariable Long roomId, @RequestBody MessageRequest request) {
        return ResponseEntity.ok().build();
    }
}

