package com.example.chat_webapp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    /**
     * GET /api/rooms
     * リクエストボディ：なし
     * 認証必要
     * 現在ログイン中ユーザーの所属ルーム一覧を取得する
     */
    @GetMapping
    public ResponseEntity<?> getUserRooms() {
        return ResponseEntity.ok().build();
    }

    /**
     * POST /api/rooms
     * リクエストボディ：{ name }
     * 認証必要
     * 新規ルーム作成
     */
    @PostMapping
    public ResponseEntity<?> createRoom(@RequestBody RoomCreateRequest request) {
        return ResponseEntity.ok().build();
    }

    /**
     * DELETE /api/rooms/{roomId}
     * リクエストボディ：なし
     * 認証必要（管理者のみ）
     * ルーム削除
     */
    @DeleteMapping("/{roomId}")
    public ResponseEntity<?> deleteRoom(@PathVariable Long roomId) {
        return ResponseEntity.ok().build();
    }

    /**
     * GET /api/rooms/search?keyword=文字列
     * リクエストボディ：なし
     * 認証必要
     * ルームを名前で検索
     */
    @GetMapping("/search")
    public ResponseEntity<?> searchRooms(@RequestParam String keyword) {
        return ResponseEntity.ok().build();
    }
}

