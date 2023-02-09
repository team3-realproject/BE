package com.example.alba_pocket.service;

import com.example.alba_pocket.dto.ChatMessageRequestDto;
import com.example.alba_pocket.dto.ChatResponseDto;
import com.example.alba_pocket.entity.ChatMessage;
import com.example.alba_pocket.entity.ChatRoom;
import com.example.alba_pocket.entity.User;
import com.example.alba_pocket.model.NotificationType;
import com.example.alba_pocket.repository.ChatMessageRepository;
import com.example.alba_pocket.repository.ChatRepositoryImpl;
import com.example.alba_pocket.repository.ChatRoomRepository;
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
    private final ChatRoomRepository chatRoomRepository;
    private final SimpMessageSendingOperations messagingTemplate;

    private final UserRepository userRepository;

    private final ChatMessageRepository chatMessageRepository;
    private final NotificationService notificationService;

    private final ChatRepositoryImpl chatRepository;


    @Transactional
    public void message(ChatMessageRequestDto message, String myNickName) throws JsonProcessingException {
        User user = userRepository.findByNickname(myNickName).orElse(new User());
        if(user.getId() == null){
            throw new IllegalArgumentException("유저정보없음");
        }

        if (ChatMessage.MessageType.ENTER.equals(message.getType())){
            List<ChatRoom> chatRooms = chatRoomRepository.findAllByRoomId(message.getRoomId());
            chatRooms.forEach(ChatRoom::plusUser);
            List<ChatMessage> chatMessages = chatRepository.falseMessage(message.getRoomId(), user.getId());
            chatMessages.forEach(ChatMessage::TrueReadUser);
            log.info("-------  ENTER! ----------");
        }
        if (ChatMessage.MessageType.QUIT.equals(message.getType())){
            List<ChatRoom> chatRooms = chatRoomRepository.findAllByRoomId(message.getRoomId());
            chatRooms.forEach(ChatRoom::minusUser);
            log.info("-------  QUIT! ----------");
        }
        if (ChatMessage.MessageType.TALK.equals(message.getType())){
            log.info("-------  TALK! ----------");
            ChatRoom chatRoom = chatRoomRepository.findByRoomIdAndUserId(message.getRoomId(), user.getId()).orElse(new ChatRoom());
            boolean countUser = chatRoom.getCountUser() == 2;
            ChatMessage chatMessage = new ChatMessage(message, user, countUser);
            chatMessageRepository.save(chatMessage);

            String Url =  "/chat/"+message.getRoomId();
            String content = message.getSender()+"님이 채팅을 보내셨습니다!";
            notificationService.send(chatRoom.getToUser(), NotificationType.CHAT,content,Url);
        }

        messagingTemplate.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
    }

    @Transactional
    public ResponseEntity<?> getMessage(String roomId) {
        List<ChatMessage> chatMessage = chatMessageRepository.findAllByRoomId(roomId);
        return new ResponseEntity<>(chatMessage.stream().map(ChatResponseDto::new).collect(Collectors.toList()), HttpStatus.OK);

    }


}