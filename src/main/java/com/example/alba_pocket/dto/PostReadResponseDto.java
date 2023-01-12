package com.example.alba_pocket.dto;

import com.example.alba_pocket.entity.Post;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class PostReadResponseDto {
    private Long postId;
    private String profileImage;
    private String userId;
    private String nickname;
    private String title;
    private String content;
    private String imgUrl;
    private String category;

    private List<PostResponseDto> postList = new ArrayList<>();

    @QueryProjection
    public PostReadResponseDto(Long postId, String profileImage, String userId, String nickname, String title, String content, String imgUrl, String category) {
        this.postId = postId;
        this.profileImage = profileImage;
        this.userId = userId;
        this.nickname = nickname;
        this.title = title;
        this.content = content;
        this.imgUrl = imgUrl;
        this.category = category;
    }


//    public PostReadResponseDto(Post post, boolean isLike, int likeCount) {
//        this.postId = post.getId();
//        this.profileImage = post.getUser().getProfileImage();
//        this.nickname = post.getUser().getNickname();
//        this.title = post.getTitle();
//        this.content = post.getContent();
//        this.imgUrl = post.getImgUrl();
//        this.category = post.getCategory();;
//    }

}
