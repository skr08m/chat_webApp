package com.example.chat_webapp.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import com.example.chat_webapp.dto.RoomCreateRequest;
import com.example.chat_webapp.entitiy.ChatRoomsModel;
import com.example.chat_webapp.entitiy.UsersModel;
import com.example.chat_webapp.service.AuthService;
import com.example.chat_webapp.service.RoomService;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {
    private final AuthService authService;
    private final RoomService roomService;

    public RoomController(AuthService authService, RoomService roomService) {
        this.authService = authService;
        this.roomService = roomService;
    }

    /**
     * GET /api/rooms
     * リクエストボディ：なし
     * 認証必要
     * 現在ログイン中ユーザーの所属ルーム一覧を取得する
     */
    @GetMapping
    public ResponseEntity<?> getUserRooms() {
        UsersModel currentUser = authService.getCurrentUser();
        Long currentUserId = currentUser.getUserId();

        List<ChatRoomsModel> userRooms = roomService.getRoomsByUserId(currentUserId);
        return ResponseEntity.ok(userRooms);

    }

    /**
     * POST /api/rooms
     * リクエストボディ：{ name, descliption }
     * 認証必要
     * 新規ルーム作成
     */
    @PostMapping
    public ResponseEntity<?> createRoom(@RequestBody RoomCreateRequest request) {
        try {
            // 現在の認証ユーザーのメールアドレスを取得
            UsersModel currentUser = authService.getCurrentUser();
            String email = currentUser.getEmail();

            // ルームを作成し、roomId を取得
            Long id = roomService.createRoom(request, email);

            // レスポンス返却
            Map<String, Object> response = new HashMap<>();
            response.put("id", id);
            response.put("name", request.getName());
            response.put("description", request.getDescliption());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "サーバーエラーが発生しました"));
        }
    }

    /**
     * DELETE /api/rooms/{roomId}
     * リクエストボディ：なし
     * 認証必要（管理者のみ）
     * ルーム削除
     */
    @DeleteMapping("/{roomId}")
    public ResponseEntity<?> deleteRoom(@PathVariable Long roomId, Principal principal) {
        if (principal == null || principal.getName() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("認証情報がありません");
        }

        String requesterEmail = principal.getName();

        try {
            roomService.deleteRoom(roomId, requesterEmail);
            return ResponseEntity.noContent().build(); // 204: 正常に削除完了
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ルームが見つかりません");
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("ユーザーが見つかりません");
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("このルームを削除する権限がありません");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("サーバーエラーが発生しました");
        }
    }

    /**
     * GET /api/rooms/search?keyword=文字列
     * リクエストボディ：なし
     * 認証必要
     * ルームを名前で検索
     */
    @GetMapping("/search")
    public ResponseEntity<?> searchRooms(@RequestParam String keyword) {
        // TODO未実装
        return ResponseEntity.ok().build();
    }
}
