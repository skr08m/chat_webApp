package com.example.chat_webapp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.chat_webapp.dto.RegisterRequest;
import com.example.chat_webapp.dto.VerifyRequest;
import com.example.chat_webapp.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
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
            return ResponseEntity.ok("ユーザーの仮登録が完了しました。");
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
