package com.example.alba_pocket.controller;

import com.example.alba_pocket.dto.ChatMessageRequestDto;
import com.example.alba_pocket.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
//@RequestMapping("/api/chat")
@Slf4j
public class ChatMessageController {

    private final ChatMessageService chatMessageService;

    //  /pub/api/chat/message 요청처리 /sub
    @MessageMapping("/api/chat/message")
    public void message(ChatMessageRequestDto message) {
        log.info("-------  MessageMapping ----------");
        chatMessageService.message(message);
    }

    //이전채팅내역조회
    @GetMapping("/api/chat/message/{roomId}")
    public ResponseEntity<?> getMessage(@PathVariable String roomId){
        return chatMessageService.getMessage(roomId);
    }


}
