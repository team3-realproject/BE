package com.example.alba_pocket.service;

import com.example.alba_pocket.dto.MsgResponseDto;
import com.example.alba_pocket.dto.PostReadResponseDto;
import com.example.alba_pocket.dto.PostRequestDto;
import com.example.alba_pocket.dto.PostResponseDto;
import com.example.alba_pocket.entity.Post;
import com.example.alba_pocket.entity.User;
import com.example.alba_pocket.errorcode.CommonStatusCode;
import com.example.alba_pocket.exception.RestApiException;
import com.example.alba_pocket.repository.LikesRepository;
import com.example.alba_pocket.repository.PostRepository;
import com.example.alba_pocket.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;

import java.util.ArrayList;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    private final LikesRepository likesRepository;

    private final S3Uploader s3Uploader;

    //작성
    @Transactional
    public ResponseEntity<?> createPost(MultipartFile file, PostRequestDto requestDto) throws IOException {
        User user = SecurityUtil.getCurrentUser();
        String imgUrl = null;
        if(!file.isEmpty()){
            imgUrl = s3Uploader.upload(file, "files");
        }
        Post post = postRepository.saveAndFlush(new Post(requestDto, user, imgUrl));
        return new ResponseEntity<>(new PostResponseDto(post), HttpStatus.OK);
    }
    //전체글조회
    @Transactional(readOnly = true)
    public ResponseEntity<?> getPosts() {
        User user = SecurityUtil.getCurrentUser();
        List<Post> posts = postRepository.findAllByOrderByCreatedAtDesc();
        return new ResponseEntity<>(posts.stream().map(post -> {
            boolean isLike = false;
            if(user != null){
                isLike = likesRepository.existsByUserIdAndPostId(user.getId(), post.getId());
            }
            int likeCount = likesRepository.countByPostId(post.getId());
            return new PostResponseDto(post, isLike, likeCount);
        }).collect(Collectors.toList()), HttpStatus.OK);
    }
    //카테고리검색
    @Transactional(readOnly = true)
    public ResponseEntity<?> categoryGetPosts(String category) {
        User user = SecurityUtil.getCurrentUser();
        List<Post> posts = postRepository.findAllByCategoryOrderByCreatedAtDesc(category);
        return new ResponseEntity<>(posts.stream().map(post -> {
            boolean isLike = false;
            if(user != null) {
                isLike = likesRepository.existsByUserIdAndPostId(user.getId(), post.getId());
            }
            int likeCount = likesRepository.countByPostId(post.getId());
            return new PostResponseDto(post, isLike, likeCount);
        }).collect(Collectors.toList()), HttpStatus.OK);
    }



    //상세조회
    @Transactional(readOnly = true)
    public ResponseEntity<?> getPost(Long postId) {
        User user = SecurityUtil.getCurrentUser();
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new RestApiException(CommonStatusCode.NO_ARTICLE)
        );
        boolean isLike = false;
        if(user != null){
            isLike = likesRepository.existsByUserIdAndPostId(user.getId(), post.getId());
        }
        int likeCount = likesRepository.countByPostId(post.getId());
        return new ResponseEntity<>(new PostResponseDto(post, isLike, likeCount), HttpStatus.OK);
    }
    //수정
    @Transactional
    public ResponseEntity<?> updatePost(Long postId, PostRequestDto requestDto) {
        User user = SecurityUtil.getCurrentUser();
        Post post = postRepository.findById(postId).orElseThrow(
                ()-> new RestApiException(CommonStatusCode.NO_ARTICLE)
        );

        if(!post.getUser().getUserId().equals(user.getUserId())){
            throw new RestApiException(CommonStatusCode.INVALID_USER_UPDATE);
        }
        post.update(requestDto);
        boolean isLike = likesRepository.existsByUserIdAndPostId(user.getId(), post.getId());
        int likeCount = likesRepository.countByPostId(post.getId());
        return new ResponseEntity<>(new PostResponseDto(post, isLike, likeCount), HttpStatus.OK);
    }
    //삭제
    public ResponseEntity<?> deletePost(Long postId) {
        User user = SecurityUtil.getCurrentUser();
        Post post = postRepository.findById(postId).orElseThrow(
                ()-> new RestApiException(CommonStatusCode.NO_ARTICLE)
        );
        if(!post.getUser().getUserId().equals(user.getUserId())){
            throw new RestApiException(CommonStatusCode.INVALID_USER_DELETE);
        }
        postRepository.deleteById(postId);
        return new ResponseEntity<>(new MsgResponseDto(CommonStatusCode.DELETE_POST), HttpStatus.OK);
    }

    
    //게시글 검색
    @Transactional(readOnly = true)
    public ResponseEntity<?> searchPost(String keyword) {
        List<Post> postList = postRepository.findAllByTitleContainingOrContentContainingOrderByCreatedAtDesc(keyword, keyword);

        List<PostReadResponseDto> postReadResponseDtoList = new ArrayList<>();

        if(postList.isEmpty()) {
            return new ResponseEntity<>(new MsgResponseDto("게시글이 없습니다."), HttpStatus.OK);
        }
        for (Post post : postList) {
            PostReadResponseDto postReadResponseDto = new PostReadResponseDto(post);
            postReadResponseDtoList.add(postReadResponseDto);
        }
        return new ResponseEntity<>(postReadResponseDtoList, HttpStatus.OK);
    }

}
