package com.example.alba_pocket.service;

import com.example.alba_pocket.dto.NotificationCountDto;
import com.example.alba_pocket.dto.NotificationResponseDto;
import com.example.alba_pocket.entity.User;
import com.example.alba_pocket.errorcode.CommonStatusCode;
import com.example.alba_pocket.exception.RestApiException;
import com.example.alba_pocket.model.Notification;
import com.example.alba_pocket.model.NotificationType;
import com.example.alba_pocket.repository.EmitterRepository;
import com.example.alba_pocket.repository.EmitterRepositoryImpl;
import com.example.alba_pocket.repository.NotificationRepository;
import com.example.alba_pocket.repository.UserRepository;
import com.example.alba_pocket.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationService {
    public static Map<String, SseEmitter> sseEmitters = new ConcurrentHashMap<>();
    private final NotificationRepository notificationRepository;

    public SseEmitter subscribe(String userId) {

        Long timeout = 60L * 1000L * 60L; // 1시간

        SseEmitter sseEmitter = new SseEmitter(timeout);
        // 생성된 emiiterId를 기반으로 emitter를 저장
        try {
            sseEmitter.send(SseEmitter.event().name("connect"));
        } catch (IOException e) {
            e.printStackTrace();
        }


        sseEmitters.put(userId, sseEmitter);
        sseEmitter.onCompletion(() -> sseEmitters.remove(userId));
        sseEmitter.onTimeout(() -> sseEmitters.remove(userId));
        sseEmitter.onError((e) -> sseEmitters.remove(userId));

        return sseEmitter;
    }


    @Async
    public void send(User receiver, NotificationType notificationType, String content, String url) {

        Notification notification = createNotification(receiver, notificationType, content, url);
        if (!notification.getNotificationType().equals(NotificationType.CHAT)) {
            notificationRepository.save(notification);
        }

        if (sseEmitters.containsKey((receiver.getUserId()))) {
            SseEmitter sseEmitter = sseEmitters.get(receiver.getUserId());
            try {
                sseEmitter.send(SseEmitter.event()
                        .name("youjung")
                        .data(NotificationResponseDto.create(notification)));
            } catch (Exception e) {
                sseEmitters.remove(receiver.getUserId());
            }
        }
    }



    private Notification createNotification(User receiver, NotificationType notificationType, String content, String url) {
        return Notification.builder()
                .receiver(receiver)
                .notificationType(notificationType)
                .content(content)
                .url(url)
                .isRead(false) // 현재 읽음상태
                .build();
    }

    //알림조회
    @Transactional(readOnly = true)
    public List<NotificationResponseDto> findAllNotifications() {
        User user = SecurityUtil.getCurrentUser();
        List<Notification> notifications = notificationRepository.findAllByUserId(user.getId());
        notifications.forEach(notification -> {log.info(String.valueOf(notification.getIsRead()));});
        return notifications.stream()
                .map(NotificationResponseDto::create)
                .collect(Collectors.toList());
    }


    public NotificationCountDto countUnReadNotifications() {
        User user = SecurityUtil.getCurrentUser();
        //유저의 알람리스트에서 ->isRead(false)인 갯수를 측정 ,
        Long count = notificationRepository.countUnReadNotifications(user.getId());
        return NotificationCountDto.builder()
                .count(count)
                .build();

    }

    @Transactional
    public void readNotification(Long notificationId) {
        //알림을 받은 사람의 id 와 알림의 id 를 받아와서 해당 알림을 찾는다.
        Optional<Notification> notification = notificationRepository.findById(notificationId);
        Notification checkNotification = notification.orElseThrow(()-> new RestApiException(CommonStatusCode.NOT_EXIST_NOTIFICATION));
        checkNotification.read(); // 읽음처리

    }

    @Transactional
    public void deleteAllByNotifications() {
        User user = SecurityUtil.getCurrentUser();
        Long receiverId = user.getId();
        notificationRepository.deleteAllByReceiverId(receiverId);

    }
    @Transactional
    public void deleteByNotifications(Long notificationId) {
        notificationRepository.deleteById(notificationId);
    }
}
