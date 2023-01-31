package com.example.alba_pocket.controller;

import com.example.alba_pocket.service.ChatRoomService1;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
@RequestMapping("/chattest")
public class ChatRoomController1 {

    private final ChatRoomService1 chatRoomService;

    @PostMapping("/rooms/{nickname}")
    public ResponseEntity<?> createRoom(@PathVariable String nickname) {
        return chatRoomService.createRoom(nickname);
    }


    @GetMapping("/rooms")
    public ResponseEntity<?> getRoomList(){
        return chatRoomService.getRoomList();
    }
}
