package com.example.chat_webapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.chat_webapp.entitiy.ChatMessagesModel;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessagesModel, Long> {

    List<ChatMessagesModel> findByRoomIdAndDeletedFalseOrderByCreatedAtAsc(Long roomId);

    // 必要に応じてページング・期間絞込みなどを追加可能
}
