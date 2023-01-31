package com.example.alba_pocket.controller;

import com.example.alba_pocket.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Controller
@RequestMapping("/api/chat")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;
    //채팅방생성
    @PostMapping("/rooms/{nickname}")
    public ResponseEntity<?> createRoom(@PathVariable String nickname) {
        return chatRoomService.createRoom(nickname);
    }

    //채팅방목록
    @GetMapping("/rooms")
    public ResponseEntity<?> getRoomList(){
        return chatRoomService.getRoomList();
    }

    //채팅방나가기
    @DeleteMapping("/rooms/{roomId}")
    public ResponseEntity<?> deleteRooms(@PathVariable String roomId){
        return chatRoomService.deleteRooms(roomId);
    }


}
