package com.example.alba_pocket.test11;

import com.example.alba_pocket.dto.RoomIdResponseDto;
import com.example.alba_pocket.entity.User;
import com.example.alba_pocket.repository.ChatRoomRepository1;
import com.example.alba_pocket.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ChatRoomRepository {

    private Map<String, ChatRoom> chatRoomMap;

    private final ChatRoomRepository1 chatRoomRepository;

    private final UserRepository userRepository;

    @PostConstruct
    private void init() {
        chatRoomMap = new LinkedHashMap<>();
    }

    public List<RoomIdResponseDto> findAllRoom() {
        // 채팅방 생성순서 최근 순으로 반환
//        List chatRooms = new ArrayList<>(chatRoomMap.values());
//        Collections.reverse(chatRooms);
        User user = userRepository.findByNickname("testuser").orElse(new User());
        List<com.example.alba_pocket.entity.ChatRoom> roomLists = chatRoomRepository.findAllByUserId(user.getId());

        return roomLists.stream().map(RoomIdResponseDto::new).collect(Collectors.toList());
    }//roomId, name return

    public ChatRoom findRoomById(String id) {
        return chatRoomMap.get(id);
    }

    public ChatRoom createChatRoom(String name) {
        ChatRoom chatRoom = ChatRoom.create(name);
        chatRoomMap.put(chatRoom.getRoomId(), chatRoom);
        return chatRoom;
    }
}