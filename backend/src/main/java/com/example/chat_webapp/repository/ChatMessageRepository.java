package com.example.chat_webapp.repository;

import com.example.chat_webapp.model.ChatMessagesModel;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessagesModel, Long> {

    List<ChatMessagesModel> findByChatRoomIdOrderByCreatedAtAsc(Long chatRoomId);

    // 必要に応じてページング・期間絞込みなどを追加可能
}
