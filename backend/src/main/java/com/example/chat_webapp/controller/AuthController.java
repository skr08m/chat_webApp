package com.example.chat_webapp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.chat_webapp.dto.LoginRequest;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    /**
     * POST /api/auth/login
     * リクエストボディ：{ username, email, password }
     * 認証不要
     * ログインしてJWTを取得する
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok().build();
    }

    /**
     * POST /api/auth/logout
     * リクエストボディ：なし
     * 認証必要
     * ログアウト（トークン破棄または無視）
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        return ResponseEntity.ok().build();
    }

    /**
     * GET /api/auth/me
     * リクエストボディ：なし
     * 認証必要
     * 現在ログイン中のユーザー情報を取得する
     */
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser() {
        return ResponseEntity.ok().build();
    }
}
