package com.example.alba_pocket.dto;

import com.example.alba_pocket.entity.Comment;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MypageCommentResponseDto {
    private String title;
    private Long commentId;
    private String comment;
    private String userId;
    private String nickname;
    private int commentLikeNum;
    private boolean isLikeComment;
    private LocalDateTime createAt;
    private String profileImage;


    public MypageCommentResponseDto(Comment comment, boolean isLike, int likeCount) {
        this.title = comment.getPost().getTitle();
        this.commentId = comment.getId();
        this.comment = comment.getComment();
        this.userId = comment.getUser().getUserId();
        this.nickname = comment.getUser().getNickname();
        this.commentLikeNum = likeCount;
        this.isLikeComment = isLike;
        this.createAt = comment.getCreatedAt();
        this.profileImage = comment.getUser().getProfileImage();
    }
}
