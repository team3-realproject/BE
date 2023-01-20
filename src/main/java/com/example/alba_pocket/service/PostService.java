package com.example.alba_pocket.service;

import com.example.alba_pocket.dto.MsgResponseDto;
import com.example.alba_pocket.dto.PostRequestDto;
import com.example.alba_pocket.dto.PostResponseDto;
import com.example.alba_pocket.entity.Post;
import com.example.alba_pocket.entity.User;
import com.example.alba_pocket.errorcode.CommonStatusCode;
import com.example.alba_pocket.exception.RestApiException;
import com.example.alba_pocket.model.PostSearchKeyword;
import com.example.alba_pocket.repository.LikesRepository;
import com.example.alba_pocket.repository.PostRepository;
import com.example.alba_pocket.repository.PostRepositoryImpl;
import com.example.alba_pocket.security.SecurityUtil;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Page;

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

    private final PostRepositoryImpl postRepositoryImpl;

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
//    무한스크롤 전체글조회
    @Transactional(readOnly = true)
    public ResponseEntity<?> getPosts(int page, int size) {
        User user = SecurityUtil.getCurrentUser();
        Pageable pageable = PageRequest.of(page, size);
        Slice<PostResponseDto> postResponseDtos = postRepositoryImpl.scrollPost(pageable, user);
        return new ResponseEntity<>(postResponseDtos, HttpStatus.OK);
    }

    //    무한스크롤 전체글조회
    @Transactional(readOnly = true)
    public ResponseEntity<?> categoryGetPosts(int page, int size, String category) {
        User user = SecurityUtil.getCurrentUser();
        Pageable pageable = PageRequest.of(page, size);
        Slice<PostResponseDto> postResponseDtos = postRepositoryImpl.scrollCategoryPost(pageable, user, category);
        return new ResponseEntity<>(postResponseDtos, HttpStatus.OK);
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
    public ResponseEntity<?> searchPost(PostSearchKeyword keyword, int page, int size) {
        List<PostResponseDto> postResponseDtoList = new ArrayList<>();
        if(keyword.getKeyword()=="") {
            return new ResponseEntity<>(postResponseDtoList, HttpStatus.OK);
        }

        Pageable pageable = PageRequest.of(page,size);
        Page<Post> postPage = postRepositoryImpl.searchPage(keyword, pageable);

        for(Post post : postPage) {
            PostResponseDto postResponseDto = new PostResponseDto(post);
            postResponseDtoList.add(postResponseDto);
        }
        return new ResponseEntity<>(postResponseDtoList, HttpStatus.OK);
    }
}

