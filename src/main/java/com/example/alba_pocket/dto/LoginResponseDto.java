package com.example.alba_pocket.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginResponseDto {

    String nickname;

    public LoginResponseDto(String nickname) {
        this.nickname = nickname;
    }
}
