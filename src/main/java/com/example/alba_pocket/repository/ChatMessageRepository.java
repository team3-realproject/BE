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

    @Query(value = "select count(read_user) from chat_message where read_user = false and room_id = :room_id and user_id not in (:user_id)", nativeQuery = true)
    Integer CountMessage(@Param("room_id") String room_id, @Param("user_id") Long user_id);

    void deleteAllByRoomId(String roomId);

    @Query(value = "select count(*) from chat_room R, chat_message C where R.user_id = :user_id and R.room_id = C.room_id and read_user = false and C.user_id not in (:user_id)", nativeQuery = true)
    Integer CountTotalMessage(@Param("user_id") Long user_id);



}
