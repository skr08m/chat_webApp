package com.example.chat_webapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.chat_webapp.entitiy.ChatRoomMembersModel;

public interface ChatRoomMemberRepository extends JpaRepository<ChatRoomMembersModel, Long> {
    // 特定ユーザーとルームのメンバー情報取得
    ChatRoomMembersModel findByUserIdAndRoomId(Long userId, Long roomId);
}
