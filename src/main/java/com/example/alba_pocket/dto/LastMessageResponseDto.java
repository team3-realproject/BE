package com.example.alba_pocket.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class LastMessageResponseDto {
    private String userId;
    private String message;
    private LocalDateTime createdAt;
}
