package com.example.alba_pocket.entity;

import com.example.alba_pocket.dto.SignupRequestDto;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@RequiredArgsConstructor
public class User extends Timestamped{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String userId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column
    private String profileImage="https://wooo96bucket.s3.ap-northeast-2.amazonaws.com/static/Default_pfp.jpg";

    public User(SignupRequestDto requestDto, String password) {
        this.userId = requestDto.getUserId();
        this.password = password;
        this.nickname = requestDto.getNickname();
    }

    public void updateUser(String imgUrl, String nickname) {
        this.profileImage=imgUrl;
        this.nickname=nickname;
    }

    @Builder
    public User(String kakaoEmail , String nickname, String password, String profileImage) {
        this.userId = kakaoEmail;
        this.nickname = nickname;
        this.password = password;
        this.profileImage = profileImage;
    }
}