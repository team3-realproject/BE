package com.example.alba_pocket.test11;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@Controller
public class ChatController {

    private final SimpMessageSendingOperations messagingTemplate;

    private final ChatService chatService;

    @MessageMapping("/chat/message")
    public ResponseEntity<?> message(ChatMessage message) {
//        if (ChatMessage.MessageType.ENTER.equals(message.getType()))
//            message.setMessage(message.getSender() + "님이 입장하셨습니다.");
//        messagingTemplate.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
        return chatService.message(message);
    }
}
