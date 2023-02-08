package com.example.alba_pocket.dto;

import com.example.alba_pocket.entity.Comment;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class MypageCommentResponseDto {
    private String title;

    private Long postId;
    private Long commentId;
    private String comment;
    private String userId;
    private String nickname;
    private Long commentLikeNum;
    private boolean isLikeComment;
    private LocalDateTime createAt;
    private String profileImage;


    public MypageCommentResponseDto(Comment comment, boolean isLike, Long likeCount) {
        this.title = comment.getPost().getTitle();
        this.postId = comment.getPost().getId();
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
