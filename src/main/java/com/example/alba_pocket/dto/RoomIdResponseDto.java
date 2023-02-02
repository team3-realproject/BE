package com.example.alba_pocket.dto;

import com.example.alba_pocket.entity.ChatMessage;
import com.example.alba_pocket.entity.ChatRoom;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class RoomIdResponseDto {
    private String roomId;

    private String nickname;

    private String profileImage;

    private String lastMessage;

    private LocalDateTime createdAt;

    public RoomIdResponseDto(ChatRoom roomList, ChatMessage chatMessage) {
        this.roomId = roomList.getRoomId();
        this.nickname = roomList.getToUser().getNickname();
        this.profileImage = roomList.getToUser().getProfileImage();
        this.lastMessage = chatMessage.getMessage();
        this.createdAt = chatMessage.getCreatedAt();
    }



}
