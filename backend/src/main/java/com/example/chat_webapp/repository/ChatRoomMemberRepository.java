package com.example.chat_webapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.chat_webapp.entitiy.ChatRoomMembersModel;

public interface ChatRoomMemberRepository extends JpaRepository<ChatRoomMembersModel, Long> {

    // あるユーザーの所属しているチャットルームメンバー情報取得
    List<ChatRoomMembersModel> findByUserId(Long userId);

    // あるチャットルームのメンバー一覧取得
    List<ChatRoomMembersModel> findByRoomId(Long roomId);

    // 特定ユーザーとルームのメンバー情報取得
    ChatRoomMembersModel findByUserIdAndRoomId(Long userId, Long roomId);
}
