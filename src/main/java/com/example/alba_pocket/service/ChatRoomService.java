package com.example.alba_pocket.service;

import com.example.alba_pocket.dto.ChatResponseDto;
import com.example.alba_pocket.entity.ChatRoom;
import com.example.alba_pocket.entity.User;
import com.example.alba_pocket.repository.ChatMessageRepository;
import com.example.alba_pocket.repository.ChatRepositoryImpl;
import com.example.alba_pocket.repository.ChatRoomRepository;
import com.example.alba_pocket.repository.UserRepository;
import com.example.alba_pocket.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;

    private final ChatRepositoryImpl chatRepositoryImpl;
    private final ChatMessageRepository chatMessageRepository;





    @Transactional
    public ResponseEntity<?> createRoom(String nickname) {
        User user = SecurityUtil.getCurrentUser();
        User user1 = userRepository.findByNickname(nickname).orElseThrow(
                () -> new IndexOutOfBoundsException("유저가존재하지않습니다.")
        );
        List<String> getRoomId = chatRepositoryImpl.getRoomId(user.getId(), user1.getId());
        System.out.println(getRoomId);
        if (getRoomId.size()>0) {
            System.out.println("채팅한적있음");
            return new ResponseEntity<>(getRoomId.get(0), HttpStatus.OK);
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

//    RoomIdResponseDto::new

    @Transactional
    public ResponseEntity<?> getRoomList() {
        User user = SecurityUtil.getCurrentUser();
        List<Map<Object, Object>> lists = chatRoomRepository.findRoomList(user.getId());

        return new ResponseEntity<>(lists.stream().map(list->{
            Long toUserId = Long.valueOf(String.valueOf(list.get("to_user_id")));
            User toUser = userRepository.findById(toUserId).orElse(new User());
            Long count = chatRepositoryImpl.CountMessage(String.valueOf(list.get("room_id")), user.getId());
            return new ChatResponseDto.RoomIdResponseDto(list, toUser, count);
        }).collect(Collectors.toList()), HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> deleteRooms(String roomId) {
        User user = SecurityUtil.getCurrentUser();
        //해당룸아이디 모두삭제 추후에 수정필요
        chatRoomRepository.deleteAllByRoomId(roomId);
        chatMessageRepository.deleteAllByRoomId(roomId);
        return new ResponseEntity<>("방삭제완료",  HttpStatus.OK);
    }

    public ResponseEntity<?> TotalCountMessage() {
        User user = SecurityUtil.getCurrentUser();
        Long count = chatRepositoryImpl.CountTotalMessage(user.getId());
        return new ResponseEntity<>(new ChatResponseDto.TotalCountMessageDto(count), HttpStatus.OK);
    }
}
