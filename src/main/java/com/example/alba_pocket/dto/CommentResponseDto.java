package com.example.alba_pocket.dto;

import com.example.alba_pocket.entity.Comment;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class CommentResponseDto {
    private Long commentId;
    private String comment;
    private String userId;
    private String nickname;
    private int commentLikeNum;
    private boolean isLikeComment;
    private LocalDateTime createAt;
    private String profileImage;

    public CommentResponseDto(Comment save) {
        this.commentId = save.getId();
        this.comment = save.getComment();
        this.userId = save.getUser().getUserId();
        this.nickname = save.getNickname();
        this.commentLikeNum = 0;
        this.isLikeComment = false;
        this.createAt = save.getCreatedAt();
        this.profileImage = save.getUser().getProfileImage();
    }

    
    public CommentResponseDto(Comment comment, boolean isLike, int likeCount) {
        this.commentId = comment.getId();
        this.comment = comment.getComment();
        this.userId = comment.getUser().getUserId();
        this.nickname = comment.getUser().getNickname();
        this.commentLikeNum = likeCount;
        this.isLikeComment = isLike;
        this.createAt = comment.getCreatedAt();
        this.profileImage = comment.getUser().getProfileImage();
    }


    @Getter
    @NoArgsConstructor
    public static class CommentCreateResponseDto {

        private Long commentId;

        public CommentCreateResponseDto(Long commentId) {
            this.commentId = commentId;
        }
    }
}
