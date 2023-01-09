package com.example.alba_pocket.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginResponseDto {

    String userId;

    public LoginResponseDto(String userId) {
        this.userId = userId;
    }
}
