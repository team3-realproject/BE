//package com.example.alba_pocket.test11;
//
//import com.example.alba_pocket.entity.ChatMessage;
//import com.example.alba_pocket.repository.ChatMessageRepository;
//import com.example.alba_pocket.repository.UserRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.messaging.simp.SimpMessageSendingOperations;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//@Service
//@RequiredArgsConstructor
//public class ChatService {
//
//    private final SimpMessageSendingOperations messagingTemplate;
//
//    private final UserRepository userRepository;
//
//    private final ChatMessageRepository chatMessageRepository;
//
//    @Transactional
//    public ResponseEntity<?> message(ChatMessage message) {
////        User user = userRepository.findByNickname("testuser").orElse(new User());
////        if (ChatMessage.MessageType.ENTER.equals(message.getType())){
////            message.setMessage(message.getSender() + "님이 입장하셨습니다.");
////            com.example.alba_pocket.entity.ChatMessage chatMessage = chatMessageRepository.findByRoomId(message.getRoomId());
////            return new ResponseEntity<>(new ChatResponseDto(chatMessage), HttpStatus.OK);
////        }
//////        if (ChatMessage.MessageType.TALK.equals(message.getType())){
//////            com.example.alba_pocket.entity.ChatMessage chatMessage = new com.example.alba_pocket.entity.ChatMessage(message, user);
//////            chatMessageRepository.save(chatMessage);
//////        }
////        messagingTemplate.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
//
//        return new ResponseEntity<>("dd", HttpStatus.OK);
//    }
//}
