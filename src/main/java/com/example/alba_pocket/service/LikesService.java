package com.example.alba_pocket.service;

import com.example.alba_pocket.dto.CommentCreateResponseDto;
import com.example.alba_pocket.dto.MsgResponseDto;
import com.example.alba_pocket.entity.*;
import com.example.alba_pocket.errorcode.CommonStatusCode;
import com.example.alba_pocket.exception.RestApiException;
import com.example.alba_pocket.model.NotificationType;
import com.example.alba_pocket.repository.CommentLikesRepository;
import com.example.alba_pocket.repository.CommentRepository;
import com.example.alba_pocket.repository.LikesRepository;
import com.example.alba_pocket.repository.PostRepository;
import com.example.alba_pocket.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class LikesService {

    private final LikesRepository likesRepository;

    private final PostRepository postRepository;

    private final CommentRepository commentRepository;

    private final CommentLikesRepository commentLikesRepository;

    private final NotificationService notificationService;

    @Transactional
    public ResponseEntity<?> postLike(Long postId) {
        User user = SecurityUtil.getCurrentUser();
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new RestApiException(CommonStatusCode.NO_ARTICLE)
        );
        Likes like = likesRepository.findByUserIdAndPostId(user.getId(), post.getId()).orElse(new Likes());

        String Url =  "/post/"+post.getId();

        String content = user.getNickname()+"님이 게시글에 좋아요을 눌렀습니다!";

        NotificationType category = null;
        if (post.getCategory().equals("free")) {
            category = NotificationType.FREEPOST;
        } else if (post.getCategory().equals("partTime")) {
            category = NotificationType.PARTTIMEPOST;
        } else if (post.getCategory().equals("cover")) {
            category = NotificationType.COVERPOST;
        }


        if(like.getId() == null){
            //본인의 게시글에 좋아요를 남길때는 알림을 보낼 필요가 없다.
            if(!Objects.equals(user.getId(), post.getUser().getId())) {
                notificationService.send(post.getUser(), category, content, Url);
            }
            Likes likes = new Likes(user, post);
            likesRepository.save(likes);
            return new ResponseEntity<>(new MsgResponseDto(CommonStatusCode.POST_LIKE), HttpStatus.OK);
        } else {
            likesRepository.deleteById(like.getId());
            return new ResponseEntity<>(new MsgResponseDto(CommonStatusCode.POST_LIKE_CANCEL), HttpStatus.OK);
        }


    }
    @Transactional
    public ResponseEntity<?> commentLike(Long commentId) {
        User user = SecurityUtil.getCurrentUser();
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new RestApiException(CommonStatusCode.NO_COMMENT)
        );
        CommentLikes commentLikes = commentLikesRepository.findByUserIdAndCommentId(user.getId(), comment.getId()).orElse(new CommentLikes());

        String Url =  "/post/"+comment.getPost().getId();

        String content = user.getNickname()+"님이 댓글에 좋아요을 눌렀습니다!";

        NotificationType category = null;
        if (comment.getPost().getCategory().equals("free")) {
            category = NotificationType.FREEPOST;
        } else if (comment.getPost().getCategory().equals("partTime")) {
            category = NotificationType.PARTTIMEPOST;
        } else if (comment.getPost().getCategory().equals("cover")) {
            category = NotificationType.COVERPOST;
        }

        if(commentLikes.getId() == null){
            //본인의 댓글에 좋아요를 남길때는 알림을 보낼 필요가 없다.
            if(!Objects.equals(user.getId(), comment.getPost().getUser().getId())) {
                notificationService.send(comment.getPost().getUser(), category, content, Url);
            }

            CommentLikes commentLike = new CommentLikes(user, comment);
            commentLikesRepository.save(commentLike);
            return new ResponseEntity<>(new MsgResponseDto(CommonStatusCode.COMMENT_LIKE), HttpStatus.OK);
        } else {
            commentLikesRepository.deleteById(commentLikes.getId());
            return new ResponseEntity<>(new MsgResponseDto(CommonStatusCode.COMMENT_LIKE_CANCEL), HttpStatus.OK);
        }


    }
}
