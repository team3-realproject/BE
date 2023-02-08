package com.example.alba_pocket.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

//쿼리dsl관련dto
@Getter
@Setter
public class ChatRoomListResponseDto {
    String nickname;
    String profileImage;
    String message;
    LocalDateTime createdAt;
}
