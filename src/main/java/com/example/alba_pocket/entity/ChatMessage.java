package com.example.alba_pocket.entity;

import com.example.alba_pocket.dto.ChatMessageRequestDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

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

    private LocalDateTime createdAt;


    public ChatMessage(ChatMessageRequestDto message, User user) {
        this.roomId = message.getRoomId();
        this.message = message.getMessage();
        this.user = user;
        this.createdAt = LocalDateTime.now();
    }
}
