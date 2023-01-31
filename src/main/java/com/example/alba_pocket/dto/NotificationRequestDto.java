package com.example.alba_pocket.dto;

import com.example.alba_pocket.entity.User;
import com.example.alba_pocket.model.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRequestDto {
    private User receiver;
    private NotificationType notificationType;
    private String content;
    private String url;


}