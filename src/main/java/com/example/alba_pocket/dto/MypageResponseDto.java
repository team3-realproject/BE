package com.example.alba_pocket.dto;

import com.example.alba_pocket.entity.User;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

@Getter
public class MypageResponseDto {

    private String userId;
    private String nickname;
    private String profileImage;
    private List<Page<PostResponseDto>> pageable = new ArrayList<>();

    public MypageResponseDto(User user) {
        this.userId = user.getUserId();
        this.nickname = user.getNickname();
        this.profileImage = user.getProfileImage();
    }

    public void addPost(Page<PostResponseDto> postResponseDto) {pageable.add(postResponseDto);}
}
