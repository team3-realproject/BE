package com.example.alba_pocket.repository;

import com.example.alba_pocket.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    List<ChatRoom> findAllByRoomId(String roomId);
    Optional<ChatRoom> findByRoomId(String roomId);

    List<ChatRoom> findAllByUserId(Long userId);

    @Query(value = "select message, M.room_id, to_user_id, user_id, created_at from (\n" +
            "    select room_id, to_user_id from chat_room where user_id = :user_id) R, (\n" +
            "        select * from (\n" +
            "            select * from chat_message where (room_id, created_at) in (select room_id, max(created_at) as a from chat_message group by room_id)) A)\n" +
            "        M where M.room_id = R.room_id order by created_at desc;", nativeQuery = true)
    List<Map<Object, Object>> findRoomList(@Param("user_id") Long user_id);

    void deleteAllByRoomId(String roomId);

    Optional<ChatRoom> findByRoomIdAndUserId(String roomId, Long user_id);

}
