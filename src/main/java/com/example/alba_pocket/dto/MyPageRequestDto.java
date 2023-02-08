package com.example.alba_pocket.dto;

import com.example.alba_pocket.entity.Comment;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

@Getter
public class MyPageRequestDto {

    @Getter
    @Setter
    @NoArgsConstructor
    public static class MyPageAttributeRequestDto {
        private MultipartFile file;
        @Pattern(regexp = "^[A-za-z0-9가-힣]{5,10}$", message = "닉네임은 5~10글자의 영어대소문자, 숫자, 한글만 입력할 수 있습니다.")
        private String nickname;

    }

    @Getter
    public static class MyPageCommentResponseDto {
        private String title;
        private Long postId;
        private Long commentId;
        private String comment;
        private String userId;
        private String nickname;
        private int commentLikeNum;
        private boolean isLikeComment;
        private LocalDateTime createAt;
        private String profileImage;

        public MyPageCommentResponseDto(Comment comment, boolean isLike, int likeCount) {
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

    @Getter
    public static class MyPageDeleteRequestDto {

        private Long[] commentIdList;

    }

}
