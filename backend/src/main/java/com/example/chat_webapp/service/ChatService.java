package com.example.chat_webapp.service;

import com.example.chat_webapp.entitiy.ChatMessagesModel;
import com.example.chat_webapp.repository.ChatMessageRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatService {

    private final ChatMessageRepository chatMessageRepository;

    public ChatService(ChatMessageRepository chatMessageRepository) {
        this.chatMessageRepository = chatMessageRepository;
    }

    //メッセージ保存
    public ChatMessagesModel saveMessage(ChatMessagesModel message) {
        return chatMessageRepository.save(message);
    }

    //履歴取得
    public List<ChatMessagesModel> getMessagesByRoomId(Long roomId) {
        return chatMessageRepository.findByRoomIdAndDeletedFalseOrderByCreatedAtAsc(roomId);
    }

}
