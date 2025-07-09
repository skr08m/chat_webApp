package com.example.chat_webapp.repository;

import com.example.chat_webapp.model.ChatRoomsModel;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ChatRoomRepository extends JpaRepository<ChatRoomsModel, Long> {

    List<ChatRoomsModel> findByNameContainingIgnoreCase(String keyword);

    // ユーザーIDから所属ルーム一覧を取得（JPQLやネイティブクエリで実装も可能）
}
