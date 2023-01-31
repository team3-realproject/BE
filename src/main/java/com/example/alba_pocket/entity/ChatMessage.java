package com.example.alba_pocket.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@RequiredArgsConstructor
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn (name = "user_id")
    private User user;

    private String roomId;

    private String message;


    public ChatMessage(com.example.alba_pocket.test11.ChatMessage message, User user) {
        this.roomId = message.getRoomId();
        this.message = message.getMessage();
        this.user = user;
    }
}
