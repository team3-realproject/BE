package com.example.alba_pocket.service;

import com.example.alba_pocket.dto.*;
import com.example.alba_pocket.entity.Comment;
import com.example.alba_pocket.entity.Post;
import com.example.alba_pocket.entity.User;
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
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MyPageService {

    private final UserRepository userRepository;
    private final LikesRepository likesRepository;
    private final PostRepository postRepository;
    private final S3Uploader s3Uploader;
    private final CommentRepository commentRepository;
    private final PostRepositoryImpl postRepositoryImpl;


    @Transactional
    public ResponseEntity<?> getMypage() {
        User user = SecurityUtil.getCurrentUser();
        MypageResponseDto mypageResponseDto = new MypageResponseDto(user);
        List<Post> posts = postRepository.findByUserOrderByCreatedAt(user);
        for (Post post : posts) {
            boolean isLike = false;
            if(user != null){
                isLike = likesRepository.existsByUserIdAndPostId(user.getId(), post.getId());
            }
            int likeCount = likesRepository.countByPostId(post.getId());
            int commentCount = commentRepository.countByPostId(post.getId());
            mypageResponseDto.addPost(new PostResponseDto(post, isLike, likeCount, commentCount));
        }

        return new ResponseEntity<>(mypageResponseDto, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> updateMypage(MypageAttributeRequestDto mypageAttributeRequestDto) throws IOException {
        User user = SecurityUtil.getCurrentUser();
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

        return new ResponseEntity<>(new MsgResponseDto("회원정보가 수정되었습니다."), HttpStatus.OK);
    }

    //마이페이지 내가 좋아요 한 게시글 조회
    @Transactional
    public ResponseEntity<?> likeMypage(int page, int size) {
        User user = SecurityUtil.getCurrentUser();
        Pageable pageable = PageRequest.of(page, size);
        Page<PostResponseDto> postPage = postRepositoryImpl.myLikePostPage(user, pageable);
        return new ResponseEntity<> (postPage, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> commentMypage(int page, int size) {
        User user = SecurityUtil.getCurrentUser();
        Pageable pageable = PageRequest.of(page, size);
        Page<PostResponseDto> commentPage = postRepositoryImpl.myCommentPostPage(user, pageable);
        return new ResponseEntity<>(commentPage, HttpStatus.OK);
    }
}
