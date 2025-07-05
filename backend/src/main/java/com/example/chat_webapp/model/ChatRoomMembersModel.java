package com.example.chat_webapp.model;

import jakarta.persistence.*;

@Entity
@Table(name = "chat_room_members")
public class ChatRoomMembersModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "room_id", nullable = false)
    private Long roomId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "is_admin", nullable = false)
    private boolean isAdmin = false;

    // getter/setter省略
}
