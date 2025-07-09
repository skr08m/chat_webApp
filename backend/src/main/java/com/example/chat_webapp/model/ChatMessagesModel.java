package com.example.chat_webapp.model;

import jakarta.persistence.*;
import lombok.Setter;

import java.time.ZonedDateTime;

@Entity
@getter
@Setter
@Table(name = "chat_messages")
public class ChatMessagesModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "room_id", nullable = false)
    private Long roomId;

    @Column(name = "sender_id", nullable = false)
    private Long senderId;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(name = "created_at", nullable = false)
    private ZonedDateTime createdAt = ZonedDateTime.now();

    @Column(name = "deleted", nullable = false)
    private boolean deleted = false;

    // getter/setter 省略
}
