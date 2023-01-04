package com.example.alba_pocket.dto;

import com.example.alba_pocket.entity.Comment;
import com.example.alba_pocket.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class CommentResponseDto {
    private Long commentId;
    private String comment;
    private String nickname;
    private int commentLikeNum;
    private boolean isLikeComment;
    private LocalDateTime createAt;

    public CommentResponseDto(Comment save) {
        this.commentId = save.getId();
        this.comment = save.getComment();
        this.nickname = save.getNickname();
        this.commentLikeNum = 0;
        this.isLikeComment = false;
        this.createAt = save.getCreatedAt();
    }

    public CommentResponseDto(Comment comment, boolean isLike, int likeCount, User author) {
        this.commentId = comment.getId();
        this.comment = comment.getComment();
        this.nickname = author.getNickname();
        this.commentLikeNum = likeCount;
        this.isLikeComment = isLike;
        this.createAt = comment.getCreatedAt();
    }
}
