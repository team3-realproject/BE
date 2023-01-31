package com.example.alba_pocket.repository;

import com.example.alba_pocket.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    ChatMessage findByRoomId(String roomId);
    List<ChatMessage> findAllByRoomId(String roomId);



}
