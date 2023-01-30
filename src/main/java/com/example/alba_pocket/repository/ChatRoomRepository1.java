package com.example.alba_pocket.repository;

import com.example.alba_pocket.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository1 extends JpaRepository<ChatRoom, Long> {
    List<ChatRoom> findAllByRoomId(String roomId);

    List<ChatRoom> findAllByUserId(Long userId);

    @Query(value = "select room_id from chat_room where user_id = :user_id OR user_id = :user_id2 group by room_id HAVING COUNT(room_id)>1", nativeQuery = true)
    Optional<String> getRoomId(@Param("user_id") Long user_id, @Param("user_id2") Long user_id2);


}
