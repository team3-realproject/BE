package com.example.alba_pocket.controller;

import com.example.alba_pocket.dto.MsgResponseDto;
import com.example.alba_pocket.dto.NotificationCountDto;
import com.example.alba_pocket.dto.NotificationResponseDto;
import com.example.alba_pocket.entity.User;
import com.example.alba_pocket.errorcode.UserStatusCode;
import com.example.alba_pocket.exception.RestApiException;
import com.example.alba_pocket.repository.UserRepository;
import com.example.alba_pocket.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;



@RestController
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    private final UserRepository userRepository;
    // MIME TYPE - text/event-stream 형태로 받아야함.
    // 클라이어트로부터 오는 알림 구독 요청을 받는다.
    // 로그인한 유저는 SSE 연결

    @GetMapping(value = "/subscribe/{userId}", produces = "text/event-stream")
    public SseEmitter subscribe(@PathVariable String userId) {

        return notificationService.subscribe(userId);
    }
    //알림조회
    @GetMapping(value = "/notifications")
    public List<NotificationResponseDto> findAllNotifications() {
        return notificationService.findAllNotifications();
    }

    //전체목록 알림 조회에서 해당 목록 클릭 시 읽음처리 ,
    @PostMapping("/notification/read/{notificationId}")
    public void readNotification(@PathVariable Long notificationId){
        notificationService.readNotification(notificationId);

    }
    //알림 조회 - 구독자가 현재 읽지않은 알림 갯수
    @GetMapping(value = "/notifications/count")
    public NotificationCountDto countUnReadNotifications() {
        return notificationService.countUnReadNotifications();
    }

    //알림 전체 삭제
    @DeleteMapping(value = "/notifications/delete")
    public ResponseEntity<Object> deleteNotifications(){
        notificationService.deleteAllByNotifications();
        return new ResponseEntity<>(new MsgResponseDto("알림 목록 전체삭제 성공"), HttpStatus.OK);
    }
    //단일 알림 삭제
    @DeleteMapping(value = "/notifications/delete/{notificationId}")
    public ResponseEntity<Object> deleteNotification(@PathVariable Long notificationId){

        notificationService.deleteByNotifications(notificationId);
        return new ResponseEntity<>(new MsgResponseDto("알림 목록 삭제 성공"), HttpStatus.OK);
    }
}


