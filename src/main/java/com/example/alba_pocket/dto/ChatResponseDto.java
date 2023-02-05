package com.example.alba_pocket.dto;

import com.example.alba_pocket.entity.ChatMessage;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ChatResponseDto {

    private String message;

    private String sender;//닉네임
    private String profileImage;
    private LocalDateTime createdAt;

    private boolean readUser;


    public ChatResponseDto(ChatMessage chatMessage) {
        this.message = chatMessage.getMessage();
        this.sender = chatMessage.getUser().getNickname();
        this.profileImage = chatMessage.getUser().getProfileImage();
        this.createdAt = chatMessage.getCreatedAt();
        this.readUser = chatMessage.isReadUser();
    }


}
