package com.example.alba_pocket.repository;

import com.example.alba_pocket.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    ChatMessage findByRoomId(String roomId);

}
