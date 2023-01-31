package com.example.alba_pocket.service;

import com.example.alba_pocket.dto.CommentCreateResponseDto;
import com.example.alba_pocket.dto.CommentRequestDto;
import com.example.alba_pocket.dto.CommentResponseDto;
import com.example.alba_pocket.dto.MsgResponseDto;
import com.example.alba_pocket.entity.Comment;
import com.example.alba_pocket.entity.Post;
import com.example.alba_pocket.entity.User;
import com.example.alba_pocket.errorcode.CommonStatusCode;
import com.example.alba_pocket.errorcode.UserStatusCode;
import com.example.alba_pocket.exception.RestApiException;
import com.example.alba_pocket.model.NotificationType;
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
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final CommentLikesRepository commentLikesRepository;

    private final NotificationService notificationService;

    //댓글작성
    @Transactional
    public ResponseEntity<?> createComment(Long postId, CommentRequestDto requestDto) {
        User user = SecurityUtil.getCurrentUser();
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new RestApiException(CommonStatusCode.NO_ARTICLE)
        );
        Comment save = new Comment(user, post, requestDto);
        commentRepository.saveAndFlush(save);

        /*
         댓글이 달린 post 정보와 로그인한 user 정보가 일치하는 댓글들만 list 에 담고,
         가장 마지막에 달린 댓글 id를 commentId에 담아서 보내줌
         (방금 단 댓글이 해당 유저가 그 post 에 쓴 가장 마지막 댓글임을 이용)
         */
        List<Comment> findCommentByPost = commentRepository.findAllByPostAndUser(post, user);
        Long commentId = findCommentByPost.get(findCommentByPost.size() - 1).getId();
        //해당 댓글로 이동하는 url
        String Url = "post/"+post.getId();
        //댓글 생성 시 모집글 작성 유저에게 실시간 알림 전송 ,
        String content = post.getUser().getNickname()+"님! 게시글에 댓글 알림이 도착했어요!";

        //본인의 게시글에 댓글을 남길때는 알림을 보낼 필요가 없다.
        if(!Objects.equals(SecurityUtil.getCurrentUser().getId(), post.getUser().getId())) {
            notificationService.send(post.getUser(), NotificationType.REPLY, content, Url);
        }
        return new ResponseEntity<>(new CommentCreateResponseDto(commentId), HttpStatus.OK);
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
        List<Comment> comments = commentRepository.findAllByPostIdOrderByCreatedAtAsc(postId);
        return comments.stream().map(comment -> {
            boolean isLike = false;
            if(user != null) {
                isLike = commentLikesRepository.existsByUserIdAndCommentId(user.getId(), comment.getId());
            }

            int likeCount = commentLikesRepository.countByCommentId(comment.getId());
            return new CommentResponseDto(comment, isLike, likeCount);
        }).collect(Collectors.toList());


    }


}
