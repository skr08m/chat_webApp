package com.example.chat_webapp.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.chat_webapp.dto.RegisterRequest;
import com.example.chat_webapp.dto.VerifyRequest;
import com.example.chat_webapp.entitiy.UsersModel;
import com.example.chat_webapp.service.AuthService;
import com.example.chat_webapp.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final AuthService authService;

    public UserController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    /**
     * POST /api/users/register
     * リクエストボディ：{ email, password, username}
     * 認証不要
     * 新規ユーザー登録（仮登録）
     * 注意事項表示あり
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            userService.registerUser(request);
            String token = authService.login(request.getEmail(), request.getPassword());
            System.out.println("ログイン成功。トークン発行済み");
            return ResponseEntity.ok(Map.of("token", token));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("ユーザー登録中にエラーが発生しました。");
        }
    }

    /**
     * POST /api/users/verify
     * リクエストボディ：{ token }
     * 認証不要
     * メール認証トークンによる本登録処理
     */
    @PostMapping("/verify")
    public ResponseEntity<?> verifyEmail(@RequestBody VerifyRequest request) {
        // TODO未実装
        return ResponseEntity.ok().build();
    }
}
