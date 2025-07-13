package com.example.chat_webapp.dto;

public class ChatMessage {
    private String sender;    // 送信者の名前やID
    private String content;   // メッセージ本文
    private String timestamp; // 送信時刻（ISO8601形式など）
    private String roomId;    // ルームID（必要なら）
}
