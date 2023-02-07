package com.example.alba_pocket.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserResponseDto {

    @Getter
    @NoArgsConstructor
    public static class LoginResponseDto {
        String userId;
        String nickname;
        public LoginResponseDto(String userId, String nickname) {
            this.userId = userId;
            this.nickname = nickname;
        }
    }



}
