package com.example.chat_webapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.chat_webapp.entitiy.ChatRoomsModel;

import java.util.List;

public interface ChatRoomRepository extends JpaRepository<ChatRoomsModel, Long> {

    List<ChatRoomsModel> findByNameContainingIgnoreCase(String name);

    // ユーザーIDから所属ルーム一覧を取得（JPQLやネイティブクエリで実装も可能）
}
