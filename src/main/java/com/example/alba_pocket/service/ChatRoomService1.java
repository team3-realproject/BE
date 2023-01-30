package com.example.alba_pocket.service;

import com.example.alba_pocket.dto.RoomIdResponseDto;
import com.example.alba_pocket.entity.ChatRoom;
import com.example.alba_pocket.entity.User;
import com.example.alba_pocket.repository.ChatRoomRepository1;
import com.example.alba_pocket.repository.UserRepository;
import com.example.alba_pocket.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatRoomService1 {

    private final ChatRoomRepository1 chatRoomRepository;

    private final UserRepository userRepository;




    @Transactional
    public ResponseEntity<?> createRoom(String nickname) {
        User user = SecurityUtil.getCurrentUser();
        User user1 = userRepository.findByNickname(nickname).orElseThrow(
                () -> new IndexOutOfBoundsException("유저가존재하지않습니다.")
        );
        Optional<String> getRoomId = chatRoomRepository.getRoomId(user.getId(), user1.getId());
        System.out.println(getRoomId);
        if (getRoomId.isPresent()) {
            System.out.println("채팅한적있음");
            return new ResponseEntity<>(getRoomId.get(), HttpStatus.OK);
        }
        String roomId = RoomIdCheck(String.valueOf(ThreadLocalRandom.current().nextInt(100000000, 1000000000)));
        ChatRoom chatRoom = new ChatRoom(user, user1, roomId);
        ChatRoom chatRoom1 = new ChatRoom(user1, user, roomId);
        chatRoomRepository.save(chatRoom);
        chatRoomRepository.save(chatRoom1);

        return new ResponseEntity<>(roomId, HttpStatus.OK);
    }

    //roomId 중복검사
    public String RoomIdCheck(String roomId) {
        String checkId = roomId;
        List<ChatRoom> chatRoom = chatRoomRepository.findAllByRoomId(checkId);
        while (!chatRoom.isEmpty()) {
            checkId = String.valueOf(ThreadLocalRandom.current().nextInt(100000000, 1000000000));
            chatRoom = chatRoomRepository.findAllByRoomId(checkId);
        }
        return checkId;
    }


    public ResponseEntity<?> testRoom() {

        return null;
    }

    public ResponseEntity<?> getRoomList() {
        User user = SecurityUtil.getCurrentUser();
        List<ChatRoom> roomLists = chatRoomRepository.findAllByUserId(user.getId());
        return new ResponseEntity<>(roomLists.stream().map(RoomIdResponseDto::new).collect(Collectors.toList()), HttpStatus.OK);
    }
}
