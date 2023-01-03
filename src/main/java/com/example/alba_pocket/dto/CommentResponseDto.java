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
}
