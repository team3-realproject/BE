package com.example.alba_pocket.dto;

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

    public RoomIdResponseDto(ChatRoom roomList) {
        this.roomId = roomList.getRoomId();
        this.nickname = roomList.getToUser().getNickname();
        this.profileImage = roomList.getToUser().getProfileImage();
        this.lastMessage = "마지막메세지";//임시로보내는데이터 수정필요
        this.createdAt = LocalDateTime.now();//임시로보내는데이터 수정필요
    }


}
