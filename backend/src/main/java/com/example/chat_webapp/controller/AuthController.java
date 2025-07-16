package com.example.chat_webapp.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import com.example.chat_webapp.dto.LoginRequest;
import com.example.chat_webapp.entitiy.UsersModel;
import com.example.chat_webapp.service.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * POST /api/auth/login
     * リクエストボディ：{ email, password }
     * 認証不要
     * ログインしてJWTを取得する
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            System.out.println("ログインAPI 呼び出し開始");
            String token = authService.login(request.getEmail(), request.getPassword());
            System.out.println("ログイン成功。トークン発行済み");
            System.out.println("発行されたトークンは、" + token);
            return ResponseEntity.ok(Map.of("token", token));
        } catch (BadCredentialsException e) {
            System.out.println("BadCredentialsException: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "メールアドレスまたはパスワードが正しくありません"));
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "サーバーエラーが発生しました"));
        }
    }

    /**
     * GET /api/auth/me
     * リクエストボディ：なし
     * 認証必要
     * 現在ログイン中のユーザー情報を取得する
     */
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser() {
        UsersModel user = authService.getCurrentUser();

        return ResponseEntity.ok(Map.of(
                "userId", user.getUserId(),
                "email", user.getEmail(),
                "username", user.getUsername()));
    }
}
