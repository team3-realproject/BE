package com.example.alba_pocket.repository;

import com.example.alba_pocket.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    ChatMessage findByRoomId(String roomId);
    List<ChatMessage> findAllByRoomId(String roomId);
    Optional<ChatMessage> findTopByRoomIdOrderByIdDesc(String roomId);

    @Query(value = "select * from chat_message where room_id = :room_id and user_id not in (:user_id) and read_user = false", nativeQuery = true)
    List<ChatMessage> falseMessage(@Param("room_id") String room_id, @Param("user_id") Long user_id);



}
