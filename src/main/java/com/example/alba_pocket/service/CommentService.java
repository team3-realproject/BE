package com.example.alba_pocket.service;

import com.example.alba_pocket.dto.CommentRequestDto;
import com.example.alba_pocket.dto.CommentResponseDto;
import com.example.alba_pocket.dto.MsgResponseDto;
import com.example.alba_pocket.entity.Comment;
import com.example.alba_pocket.entity.Post;
import com.example.alba_pocket.entity.User;
import com.example.alba_pocket.errorcode.CommonStatusCode;
import com.example.alba_pocket.exception.RestApiException;
import com.example.alba_pocket.model.NotificationType;
import com.example.alba_pocket.repository.*;
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
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    private final NotificationService notificationService;
    private final CommentRepositoryImpl commentRepositoryImpl;

    //댓글작성
    @Transactional
    public ResponseEntity<?> createComment(Long postId, CommentRequestDto requestDto) {
        User user = SecurityUtil.getCurrentUser();
        if(requestDto.getComment().length()>100) {
            throw new RestApiException(CommonStatusCode.OVER_COMMENT);
        }
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new RestApiException(CommonStatusCode.NO_ARTICLE)
        );
        Comment save = new Comment(user, post, requestDto);
        commentRepository.saveAndFlush(save);

        List<Comment> findCommentByPost = commentRepository.findAllByPostAndUser(post, user);
        Long commentId = findCommentByPost.get(findCommentByPost.size() - 1).getId();
        String Url = "/post/"+post.getId();
        String content = user.getNickname()+"님이 게시글에 댓글을 작성했습니다!";

        NotificationType category = null;
        if (post.getCategory().equals("free")) {
            category = NotificationType.FREEPOST;
        } else if (post.getCategory().equals("partTime")) {
            category = NotificationType.PARTTIMEPOST;
        } else if (post.getCategory().equals("cover")) {
            category = NotificationType.COVERPOST;
        }

        if(!Objects.equals(SecurityUtil.getCurrentUser().getId(), post.getUser().getId())) {
            notificationService.send(post.getUser(), category, content, Url);
        }
        return new ResponseEntity<>(new CommentResponseDto.CommentCreateResponseDto(commentId), HttpStatus.OK);
    }

    //댓글수정
    @Transactional
    public ResponseEntity<?> updateComment(Long commentId, CommentRequestDto requestDto) {
        User user = SecurityUtil.getCurrentUser();
        if(requestDto.getComment().length()>100) {
            throw new RestApiException(CommonStatusCode.OVER_COMMENT);
        }
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new RestApiException(CommonStatusCode.NO_COMMENT)
        );
        if(!comment.getUser().getId().equals(user.getId())) {
            throw new RestApiException(CommonStatusCode.INVALID_USER_UPDATE);
        }
        comment.update(requestDto, user);
        return new ResponseEntity<>(new CommentResponseDto(comment), HttpStatus.OK);
    }

    //댓글삭제
    @Transactional
    public ResponseEntity<?> deleteComment(Long commentId) {
        User user = SecurityUtil.getCurrentUser();
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new RestApiException(CommonStatusCode.NO_COMMENT)
        );
        if(!comment.getUser().getId().equals(user.getId())) {
            throw new RestApiException(CommonStatusCode.INVALID_USER_UPDATE);
        }
        commentRepository.deleteById(commentId);
        return new ResponseEntity<>(new MsgResponseDto(CommonStatusCode.DELETE_COMMENT), HttpStatus.OK);
    }

    
    //댓글조회
    public List<CommentResponseDto> getComments(Long postId) {
        User user = SecurityUtil.getCurrentUser();
        List<CommentResponseDto> commentList = commentRepositoryImpl.commentList(user, postId);
        return commentList;
    }


}
