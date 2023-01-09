package com.example.alba_pocket.service;

import com.example.alba_pocket.dto.CommentRequestDto;
import com.example.alba_pocket.dto.CommentResponseDto;
import com.example.alba_pocket.dto.MsgResponseDto;
import com.example.alba_pocket.entity.Comment;
import com.example.alba_pocket.entity.Post;
import com.example.alba_pocket.entity.User;
import com.example.alba_pocket.errorcode.CommonStatusCode;
import com.example.alba_pocket.errorcode.UserStatusCode;
import com.example.alba_pocket.exception.RestApiException;
import com.example.alba_pocket.repository.CommentLikesRepository;
import com.example.alba_pocket.repository.CommentRepository;
import com.example.alba_pocket.repository.PostRepository;
import com.example.alba_pocket.repository.UserRepository;
import com.example.alba_pocket.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final CommentLikesRepository commentLikesRepository;

    //댓글작성
    @Transactional
    public ResponseEntity<?> createComment(Long postId, CommentRequestDto requestDto) {
        User user = SecurityUtil.getCurrentUser();
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new RestApiException(CommonStatusCode.NO_ARTICLE)
        );
        Comment save = new Comment(user, post, requestDto);
        commentRepository.saveAndFlush(save);
        return new ResponseEntity<>(new CommentResponseDto(save), HttpStatus.OK);
    }

    //댓글수정
    @Transactional
    public ResponseEntity<?> updateComment(Long commentId, CommentRequestDto requestDto) {
        User user = SecurityUtil.getCurrentUser();
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
        List<Comment> comments = commentRepository.findAllByPostIdOrderByCreatedAtDesc(postId);
        return comments.stream().map(comment -> {
            boolean isLike = false;
            if(user != null) {
                isLike = commentLikesRepository.existsByUserIdAndCommentId(user.getId(), comment.getId());
            }
            User author = userRepository.findById(comment.getUser().getId()).orElseThrow(
                    () -> new RestApiException(UserStatusCode.NO_USER)
            );
            int likeCount = commentLikesRepository.countByCommentId(comment.getId());
            return new CommentResponseDto(comment, isLike, likeCount, author);
        }).collect(Collectors.toList());


    }
}
