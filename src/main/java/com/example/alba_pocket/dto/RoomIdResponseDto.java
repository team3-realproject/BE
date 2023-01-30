package com.example.alba_pocket.dto;

import com.example.alba_pocket.entity.ChatRoom;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RoomIdResponseDto {
    private String roomId;
    private String name;

//    private String nickname;
//
//    private String profileImage;
//
//    private String endMessage;

    public RoomIdResponseDto(ChatRoom roomList) {
        this.roomId = roomList.getRoomId();
        this.name = roomList.getToUser().getNickname();
//        this.nickname = roomList.getToUser().getNickname();
//        this.profileImage = roomList.getToUser().getProfileImage();
    }


}
