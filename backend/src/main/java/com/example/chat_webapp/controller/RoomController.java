package com.example.chat_webapp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.chat_webapp.dto.RoomCreateRequest;

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
        //TODO未実装
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
        //TODO未実装、個人はトークン情報から特定
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
        //TODO未実装
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
        //TODO未実装
        return ResponseEntity.ok().build();
    }
}
