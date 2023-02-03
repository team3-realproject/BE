package com.example.alba_pocket.dto;

import com.example.alba_pocket.model.Notification;
import com.example.alba_pocket.model.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class NotificationResponseDto {


    private Long id;

    private String content;

    private String url;

    private Boolean status;
    private NotificationType type;

    private LocalDateTime time;



    public static NotificationResponseDto create(Notification notification) {
        return new NotificationResponseDto(notification.getId(), notification.getContent(),
                notification.getUrl(), notification.getIsRead(), notification.getNotificationType(), notification.getCreatedAt());
    }


}