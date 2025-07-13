package com.example.chat_webapp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mail")
public class MailController {

    /**
     * POST /api/mail/send-verification
     * リクエストボディ：{ email }
     * 認証必要
     * 登録後にメール認証リンクを送信する
     */
    @PostMapping("/send-verification")
    public ResponseEntity<?> sendVerification(@RequestBody EmailRequest request) {
        return ResponseEntity.ok().build();
    }
}
