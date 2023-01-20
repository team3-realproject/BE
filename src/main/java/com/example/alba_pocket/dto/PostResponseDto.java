package com.example.alba_pocket.dto;

import com.example.alba_pocket.entity.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class PostResponseDto {
    private Long postId;
    private String profileImage;
    private String userId;
    private String nickname;
    private String title;
    private String content;
    private String imgUrl;
    private int postLikeNum;
    private boolean isLikePost;
    private String category;
    private int commentCount;
    private LocalDateTime createAt;
    private LocalDateTime modifiedAt;


    public PostResponseDto(Post post) {
        this.postId = post.getId();
        this.profileImage = post.getUser().getProfileImage();
        this.userId = post.getUser().getUserId();
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


    public PostResponseDto(Post post, boolean isLike, int likeCount, int commentCount) {
        this.postId = post.getId();
        this.profileImage = post.getUser().getProfileImage();
        this.userId = post.getUser().getUserId();
        this.nickname = post.getUser().getNickname();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.imgUrl = post.getImgUrl();
        this.postLikeNum = likeCount;
        this.isLikePost = isLike;
        this.category = post.getCategory();
        this.commentCount = commentCount;
        this.createAt = post.getCreatedAt();
        this.modifiedAt = post.getModifiedAt();
    }
}
