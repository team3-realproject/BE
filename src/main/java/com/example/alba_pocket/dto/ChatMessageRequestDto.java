package com.example.alba_pocket.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChatMessageRequestDto {
    public enum MessageType {
        ENTER, TALK, QUIT
    }
    private MessageType type; // 메시지 타입
    private String roomId; // 방번호
    private String sender; // 메시지 보낸사람
    private String message; // 메시지
}
