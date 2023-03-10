package com.example.alba_pocket.service;

import com.example.alba_pocket.dto.*;
import com.example.alba_pocket.entity.Comment;
import com.example.alba_pocket.entity.User;
import com.example.alba_pocket.errorcode.CommonStatusCode;
import com.example.alba_pocket.errorcode.UserStatusCode;
import com.example.alba_pocket.exception.RestApiException;
import com.example.alba_pocket.repository.*;
import com.example.alba_pocket.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class MyPageService {

    private final UserRepository userRepository;
    private final S3Uploader s3Uploader;
    private final CommentRepository commentRepository;
    private final PostRepositoryImpl postRepositoryImpl;
    private final CommentRepositoryImpl commentRepositoryImpl;


    @Transactional
    public ResponseEntity<?> getMypage(int page, int size) {
        User user = SecurityUtil.getCurrentUser();
        Pageable pageable = PageRequest.of(page, size);
        PostCondition postCondition = new PostCondition();
        postCondition.setUserId(user.getId());
        Page<PostResponseDto> getMypage = postRepositoryImpl.dynamicPagePost(pageable, user, postCondition);
        MypageResponseDto mypageResponseDto = new MypageResponseDto(user);
        mypageResponseDto.addPost(getMypage);

        return new ResponseEntity<>(mypageResponseDto, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> updateMypage(MyPageRequestDto.MyPageAttributeRequestDto mypageAttributeRequestDto) throws IOException {
        User user = SecurityUtil.getCurrentUser();
        if(mypageAttributeRequestDto.getNickname()!=null) {
            if(userRepository.existsByNickname(mypageAttributeRequestDto.getNickname())) {
                throw new RestApiException(UserStatusCode.OVERLAPPED_NICKNAME);
            }
        }
        String imgUrl = null;
        if(mypageAttributeRequestDto.getFile()!=null){
            imgUrl = s3Uploader.upload(mypageAttributeRequestDto.getFile(), "files");
        }
        if (imgUrl==null) {
            user.updateNickname(mypageAttributeRequestDto.getNickname());
        } else if (mypageAttributeRequestDto.getNickname()==null) {
            user.updateProfileImage(imgUrl);
        } else if (imgUrl!=null && mypageAttributeRequestDto.getNickname()!=null) {
            user.updateUser(imgUrl, mypageAttributeRequestDto.getNickname());
        } else { }

        userRepository.saveAndFlush(user);

        return new ResponseEntity<>(new MsgResponseDto("??????????????? ?????????????????????."), HttpStatus.OK);
    }

    //??????????????? ?????? ????????? ??? ????????? ??????
    @Transactional
    public ResponseEntity<?> likeMypage(int page, int size) {
        User user = SecurityUtil.getCurrentUser();
        Pageable pageable = PageRequest.of(page, size);
        Page<PostResponseDto> postPage = postRepositoryImpl.myLikePostPage(user, pageable);
        return new ResponseEntity<> (postPage, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> deleteMyPageComments(MyPageRequestDto.MyPageDeleteRequestDto mypageDeleteRequestDto) {
        User user = SecurityUtil.getCurrentUser();
        for (Long commentId : mypageDeleteRequestDto.getCommentIdList()) {
            Comment comment = commentRepository.findById(commentId).orElseThrow(
                    () -> new RestApiException(CommonStatusCode.NO_COMMENT)
            );
            if (comment.getUser().getUserId().equals(user.getUserId())) {
                commentRepository.deleteById(commentId);
            } else {
                throw new RestApiException(CommonStatusCode.INVALID_USER_DELETE);
            }

        }
        return new ResponseEntity<>(new MsgResponseDto("?????? ????????? ?????????????????????."), HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> commentMypage(int page, int size) {
        User user = SecurityUtil.getCurrentUser();
        Pageable pageable = PageRequest.of(page, size);
        Page<MypageCommentResponseDto> commentPage = commentRepositoryImpl.myCommentPostPage(user, pageable);

        return new ResponseEntity<>(commentPage, HttpStatus.OK);
    }
}
