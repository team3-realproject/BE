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


}
