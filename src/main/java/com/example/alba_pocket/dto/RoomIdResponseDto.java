package com.example.alba_pocket.dto;

import com.example.alba_pocket.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@Getter
@NoArgsConstructor
public class RoomIdResponseDto {
    private String roomId;

    private String nickname;

    private String profileImage;

    private String lastMessage;

    private String createdAt;

//    public RoomIdResponseDto(ChatRoom roomList, ChatMessage chatMessage) {
//        this.roomId = roomList.getRoomId();
//        this.nickname = roomList.getToUser().getNickname();
//        this.profileImage = roomList.getToUser().getProfileImage();
//        this.lastMessage = chatMessage.getMessage();
//        this.createdAt = String.valueOf(chatMessage.getCreatedAt());
//    }


    public RoomIdResponseDto(Map<Object, Object> roomList, User toUser) {
        this.roomId = (String) roomList.get("room_id");
        this.nickname = toUser.getNickname();
        this.profileImage = toUser.getProfileImage();
        this.lastMessage = String.valueOf(roomList.get("message"));
        this.createdAt = String.valueOf(roomList.get("created_at"));
    }

}
