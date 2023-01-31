package com.example.alba_pocket.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@RequiredArgsConstructor
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String roomId;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "user_id")
    private User user;

    @ManyToOne (fetch = FetchType.LAZY)
    private User toUser;

    private LocalDateTime createdAt;

    public ChatRoom(User user, User user1, String roomId) {
        this.user = user;
        this.toUser = user1;
        this.roomId = roomId;
        this.createdAt = LocalDateTime.now();
    }


}
