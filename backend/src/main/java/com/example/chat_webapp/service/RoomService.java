package com.example.chat_webapp.service;

import com.example.chat_webapp.dto.RoomCreateRequest;
import com.example.chat_webapp.entitiy.ChatRoomMembersModel;
import com.example.chat_webapp.entitiy.ChatRoomsModel;
import com.example.chat_webapp.repository.ChatRoomMemberRepository;
import com.example.chat_webapp.repository.ChatRoomRepository;
import com.example.chat_webapp.repository.UserRepository;

import jakarta.transaction.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class RoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;

    public RoomService(ChatRoomRepository chatRoomRepository, UserRepository userRepository,
            ChatRoomMemberRepository chatRoomMemberRepository) {
        this.chatRoomRepository = chatRoomRepository;
        this.userRepository = userRepository;
        this.chatRoomMemberRepository = chatRoomMemberRepository;
    }

    // ルーム作成
    @Transactional
    public Long createRoom(RoomCreateRequest roomCreateRequest, String email) {
        Long userId = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email))
                .getUserId();

        ChatRoomsModel room = new ChatRoomsModel();
        room.setName(roomCreateRequest.getName());
        room.setDescription(roomCreateRequest.getDescliption());
        room.setCreatedBy(userId);
        room.setCreatedAt(ZonedDateTime.now());

        Long roomId = chatRoomRepository.save(room).getId();

        ChatRoomMembersModel chatRoomMembersModel = new ChatRoomMembersModel();
        chatRoomMembersModel.setUserId(userId);
        chatRoomMembersModel.setRoomId(roomId);
        chatRoomMembersModel.setAdmin(true);

        chatRoomMemberRepository.save(chatRoomMembersModel);

        return roomId;
    }

    public List<ChatRoomsModel> getRoomsByUserId(Long userId) {
        return chatRoomRepository.findByCreatedBy(userId);
    }

    // ルーム削除（オーナー確認付き）
    @Transactional
    public void deleteRoom(Long roomId, String requesterEmail) {
        ChatRoomsModel room = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new NoSuchElementException("ルームが見つかりません"));

        Long requesterId = userRepository.findByEmail(requesterEmail)
                .orElseThrow(() -> new UsernameNotFoundException("ユーザーが見つかりません"))
                .getUserId();

        if (!room.getCreatedBy().equals(requesterId)) {
            throw new SecurityException("このルームを削除する権限がありません");
        }

        chatRoomRepository.deleteById(roomId);
    }
}
