package com.example.alba_pocket.dto;

import com.example.alba_pocket.entity.ChatMessage;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChatResponseDto {

    private String message;

    private String nickname;
    private String profileImage;


    public ChatResponseDto(ChatMessage chatMessage) {
        this.message = chatMessage.getMessage();
        this.nickname = chatMessage.getUser().getNickname();
        this.profileImage = chatMessage.getUser().getProfileImage();

    }


}
