package com.example.alba_pocket.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
public class KakaoUserInfoDto {
    private Long id;
    private String email;
    private String userImgUrl;

    public KakaoUserInfoDto(Long id, String email, String userImgUrl) {
        this.id = id;
        this.email = email;
        this.userImgUrl=userImgUrl;
    }
}