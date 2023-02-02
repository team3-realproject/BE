package com.example.alba_pocket.repository;

import com.example.alba_pocket.dto.ChatRoomListResponseDto;
import com.example.alba_pocket.entity.User;

import java.util.List;

public interface ChatCustomRepository {

    List<ChatRoomListResponseDto> chatRoomList(User user);
}