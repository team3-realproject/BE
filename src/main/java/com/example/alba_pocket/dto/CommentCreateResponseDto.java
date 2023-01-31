package com.example.alba_pocket.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentCreateResponseDto {

    private Long commentId;

    public CommentCreateResponseDto(Long commentId) {
        this.commentId = commentId;
    }
}