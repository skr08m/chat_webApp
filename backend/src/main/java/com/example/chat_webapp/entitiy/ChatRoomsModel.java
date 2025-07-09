package com.example.chat_webapp.model;

import jakarta.persistence.*;
import java.time.ZonedDateTime;

@Entity
@Table(name = "chat_rooms")
public class ChatRoomsModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "created_by", nullable = false)
    private Long createdBy; // FK → users.user_id

    @Column(name = "created_at", nullable = false)
    private ZonedDateTime createdAt = ZonedDateTime.now();

    // getter/setter省略
}
