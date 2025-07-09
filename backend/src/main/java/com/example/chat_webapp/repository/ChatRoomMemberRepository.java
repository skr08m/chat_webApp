package com.example.chat_webapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.chat_webapp.model.ChatRoomMembersModel;

public interface ChatRoomMemberRepository extends JpaRepository<ChatRoomMembersModel, Long> {

    // あるユーザーの所属しているチャットルームメンバー情報取得
    List<ChatRoomMembersModel> findByUserId(Long userId);

    // あるチャットルームのメンバー一覧取得
    List<ChatRoomMembersModel> findByChatRoomId(Long chatRoomId);

    // 特定ユーザーとルームのメンバー情報取得
    ChatRoomMembersModel findByUserIdAndChatRoomId(Long userId, Long chatRoomId);
}
