package com.example.alba_pocket.dto;

import com.example.alba_pocket.entity.Post;
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
    private String nickname;

    private String title;

    private String content;
    private String imgUrl;
    private int postLikeNum;
    private boolean isLikePost;
    private String category;
    private LocalDateTime createAt;
    private LocalDateTime modifiedAt;

    private List<PostResponseDto> postList = new ArrayList<>();

    public PostReadResponseDto(Post post) {
        this.postId = post.getId();
        this.profileImage = post.getUser().getProfileImage();
        this.nickname = post.getUser().getNickname();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.imgUrl = post.getImgUrl();
        this.postLikeNum = 0;
        this.isLikePost = false;
        this.category = post.getCategory();
        this.createAt = post.getCreatedAt();
        this.modifiedAt = post.getModifiedAt();
    }

    public PostReadResponseDto(Post post, boolean isLike, int likeCount) {
        this.postId = post.getId();
        this.profileImage = post.getUser().getProfileImage();
        this.nickname = post.getUser().getNickname();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.imgUrl = post.getImgUrl();
        this.postLikeNum = likeCount;
        this.isLikePost = isLike;
        this.category = post.getCategory();;
        this.createAt = post.getCreatedAt();
        this.modifiedAt = post.getModifiedAt();
    }
    
}
