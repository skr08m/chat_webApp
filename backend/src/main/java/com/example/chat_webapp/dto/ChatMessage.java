package com.example.chat_webapp.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessage {
    private String sender;    // 送信者の名前やID
    private String roomId;    // ルームID（必要なら）
    private String content;   // メッセージ本文
    private String timestamp; // 送信時刻
}
