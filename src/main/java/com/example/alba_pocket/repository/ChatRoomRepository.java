package com.example.alba_pocket.repository;

import com.example.alba_pocket.entity.ChatRoom;
import com.example.alba_pocket.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    List<ChatRoom> findAllByRoomId(String roomId);

    List<ChatRoom> findAllByUserId(Long userId);

    @Query(value = "select message, A.room_id, to_user_id, user_id, A.created_at from(\n" +
            "                 select created_at, message, room_id from chat_message where user_id = :user_id and (room_id, created_at) in (select room_id, max(created_at) as a from chat_message group by room_id)\n" +
            "             ) A, (select * from chat_room where user_id = :user_id) B where A.room_id = B.room_id order by A.created_at desc;", nativeQuery = true)
    List<Map<Object, Object>> findRoomList(@Param("user_id") Long user_id);


    @Query(value = "select room_id from chat_room where user_id = :user_id OR user_id = :user_id2 group by room_id HAVING COUNT(room_id)>1", nativeQuery = true)
    Optional<String> getRoomId(@Param("user_id") Long user_id, @Param("user_id2") Long user_id2);

    void deleteAllByRoomId(String roomId);

    @Query(value = "select u from User u join fetch ChatRoom r on u.id=r.user.id where r.roomId=:roomId and r.user.id in (:userId)")
    Optional<User> findByToUser(String roomId, Long userId);




}
