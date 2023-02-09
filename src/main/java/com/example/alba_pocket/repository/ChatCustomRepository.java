package com.example.alba_pocket.repository;

import com.example.alba_pocket.dto.ChatRoomListResponseDto;
import com.example.alba_pocket.entity.ChatMessage;
import com.example.alba_pocket.entity.User;

import java.util.List;

public interface ChatCustomRepository {

    List<ChatRoomListResponseDto> chatRoomList(User user);
    List<String> getRoomId(Long userId, Long userId2);
    Long CountMessage(String roomId, Long userId);
    List<ChatMessage> falseMessage(String roomId, Long userId);
    Long CountTotalMessage(Long userId);
}
