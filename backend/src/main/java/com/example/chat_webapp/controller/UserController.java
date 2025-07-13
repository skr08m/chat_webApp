package com.example.chat_webapp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.chat_webapp.dto.RegisterRequest;
import com.example.chat_webapp.dto.VerifyRequest;

@RestController
@RequestMapping("/api/users")
public class UserController {

    /**
     * POST /api/users/register
     * リクエストボディ：{ email, password }
     * 認証不要
     * 新規ユーザー登録（仮登録）
     * 注意事項表示あり
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        //TODO未実装
        return ResponseEntity.ok().build();
    }

    /**
     * POST /api/users/verify
     * リクエストボディ：{ token }
     * 認証不要
     * メール認証トークンによる本登録処理
     */
    @PostMapping("/verify")
    public ResponseEntity<?> verifyEmail(@RequestBody VerifyRequest request) {
        //TODO未実装
        return ResponseEntity.ok().build();
    }
}
