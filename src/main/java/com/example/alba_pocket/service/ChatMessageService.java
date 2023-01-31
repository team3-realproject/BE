package com.example.alba_pocket.service;

import com.example.alba_pocket.dto.ChatMessageRequestDto;
import com.example.alba_pocket.dto.ChatResponseDto;
import com.example.alba_pocket.entity.ChatMessage;
import com.example.alba_pocket.entity.User;
import com.example.alba_pocket.repository.ChatMessageRepository;
import com.example.alba_pocket.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatMessageService {
    private final SimpMessageSendingOperations messagingTemplate;

    private final UserRepository userRepository;

    private final ChatMessageRepository chatMessageRepository;

    @Transactional
    public void message(ChatMessageRequestDto message) {
        User user = userRepository.findByNickname("").orElse(new User());
        if (ChatMessageRequestDto.MessageType.ENTER.equals(message.getType())){
            message.setMessage(message.getSender() + "님이 입장하셨습니다.");
//            ChatMessage chatMessage = chatMessageRepository.findByRoomId(message.getRoomId());
//            return new ResponseEntity<>(new ChatResponseDto(chatMessage), HttpStatus.OK);
        }
        if (ChatMessageRequestDto.MessageType.TALK.equals(message.getType())){
            ChatMessage chatMessage = new ChatMessage(message, user);
            chatMessageRepository.save(chatMessage);
        }
        messagingTemplate.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);



//        return new ResponseEntity<>("", HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> getMessage(String roomId) {
        List<ChatMessage> chatMessage = chatMessageRepository.findAllByRoomId(roomId);
        return new ResponseEntity<>(chatMessage.stream().map(ChatResponseDto::new).collect(Collectors.toList()), HttpStatus.OK);

    }
}