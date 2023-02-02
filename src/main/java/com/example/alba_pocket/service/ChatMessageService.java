package com.example.alba_pocket.service;

import com.example.alba_pocket.dto.ChatMessageRequestDto;
import com.example.alba_pocket.dto.ChatResponseDto;
import com.example.alba_pocket.entity.ChatMessage;
import com.example.alba_pocket.entity.User;
import com.example.alba_pocket.repository.ChatMessageRepository;
import com.example.alba_pocket.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatMessageService {
    private final SimpMessageSendingOperations messagingTemplate;

    private final UserRepository userRepository;

    private final ChatMessageRepository chatMessageRepository;


    public void message(ChatMessageRequestDto message) throws JsonProcessingException {
        User user = userRepository.findByNickname("수민1123").orElse(new User());
        if(user.getId() == null){
            throw new IllegalArgumentException("유저정보없음");
        }
        log.info("메세지" + message.getMessage());
        log.info("샌더" + message.getSender());
        log.info("룸ID" + message.getRoomId());
        log.info(String.valueOf(message));
        log.info("-------  service넘어옴 ----------");
        if (ChatMessage.MessageType.ENTER.equals(message.getType())){
            message.setMessage(message.getSender() + "님이 입장하셨습니다.");
            log.info("-------  ENTER! ----------");
//            ChatMessage chatMessage = chatMessageRepository.findByRoomId(message.getRoomId());
//            return new ResponseEntity<>(new ChatResponseDto(chatMessage), HttpStatus.OK);
        }
        if (ChatMessage.MessageType.TALK.equals(message.getType())){
            log.info("-------  TALK! ----------");
            ChatMessage chatMessage = new ChatMessage(message, user);
            chatMessageRepository.save(chatMessage);
        }
        messagingTemplate.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
        log.info("----------------------------------------------------------------------------------");


//        return new ResponseEntity<>("", HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> getMessage(String roomId) {
        List<ChatMessage> chatMessage = chatMessageRepository.findAllByRoomId(roomId);
        return new ResponseEntity<>(chatMessage.stream().map(ChatResponseDto::new).collect(Collectors.toList()), HttpStatus.OK);

    }
}