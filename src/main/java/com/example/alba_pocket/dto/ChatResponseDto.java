package com.example.alba_pocket.dto;

import com.example.alba_pocket.entity.ChatMessage;
import com.example.alba_pocket.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

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

    @Getter
    @NoArgsConstructor
    public static class TotalCountMessageDto {
        private Long totalCount;

        public TotalCountMessageDto(Long count) {
            this.totalCount = count;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class RoomIdResponseDto {
        private String roomId;

        private String nickname;

        private String profileImage;

        private String lastMessage;

        private String createdAt;

        private Long count;

        public RoomIdResponseDto(Map<Object, Object> roomList, User toUser, Long count) {
            this.roomId = (String) roomList.get("room_id");
            this.nickname = toUser.getNickname();
            this.profileImage = toUser.getProfileImage();
            this.lastMessage = String.valueOf(roomList.get("message"));
            this.createdAt = String.valueOf(roomList.get("created_at"));
            this.count = count;
        }

    }




}
