package com.example.chat_webapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.chat_webapp.entitiy.ChatRoomsModel;

import java.util.List;

public interface ChatRoomRepository extends JpaRepository<ChatRoomsModel, Long> {

    List<ChatRoomsModel> findByNameContainingIgnoreCase(String name);

    List<ChatRoomsModel> findByCreatedBy(Long userId);

    // ChatRoomRepository.java
    @Query("SELECT r FROM ChatRoomsModel r JOIN ChatRoomMembersModel m ON r.id = m.roomId WHERE m.userId = :userId")
    List<ChatRoomsModel> findRoomsByUserId(@Param("userId") Long userId);

    // ユーザーIDから所属ルーム一覧を取得（JPQLやネイティブクエリで実装も可能）
}
