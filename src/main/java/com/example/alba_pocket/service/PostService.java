package com.example.alba_pocket.service;

import com.example.alba_pocket.dto.MsgResponseDto;
import com.example.alba_pocket.dto.PostCondition;
import com.example.alba_pocket.dto.PostRequestDto;
import com.example.alba_pocket.dto.PostResponseDto;
import com.example.alba_pocket.entity.Post;
import com.example.alba_pocket.entity.User;
import com.example.alba_pocket.errorcode.CommonStatusCode;
import com.example.alba_pocket.exception.RestApiException;
import com.example.alba_pocket.model.PostSearchKeyword;
import com.example.alba_pocket.repository.CommentRepository;
import com.example.alba_pocket.repository.LikesRepository;
import com.example.alba_pocket.repository.PostRepository;
import com.example.alba_pocket.repository.PostRepositoryImpl;
import com.example.alba_pocket.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    private final LikesRepository likesRepository;

    private final S3Uploader s3Uploader;

    private final PostRepositoryImpl postRepositoryImpl;
    private final CommentRepository commentRepository;

    //작성
    @Transactional
    public ResponseEntity<?> createPost(PostRequestDto requestDto) throws IOException {
        User user = SecurityUtil.getCurrentUser();
        if(requestDto.getTitle().length()>50) {
            throw new RestApiException(CommonStatusCode.OVER_TITLE);
        }
        if(requestDto.getContent().length()>500) {
            throw new RestApiException(CommonStatusCode.OVER_CONTENT);
        }
        String imgUrl = null;
        if(requestDto.getFile()!=null){
            imgUrl = s3Uploader.upload(requestDto.getFile(), "files");
        }

        Post post = postRepository.saveAndFlush(new Post(requestDto, user, imgUrl));

        return new ResponseEntity<>(new PostResponseDto(post), HttpStatus.OK);
    }
//    무한스크롤 전체글조회
    @Transactional(readOnly = true)
    public ResponseEntity<?> getPosts(int page, int size) {
        User user = SecurityUtil.getCurrentUser();
        Pageable pageable = PageRequest.of(page, size);
        PostCondition postCondition = new PostCondition();
        Slice<PostResponseDto> postResponseDtos = postRepositoryImpl.dynamicScrollPost(pageable, user, postCondition);

        return new ResponseEntity<>(postResponseDtos, HttpStatus.OK);
    }

    //    무한스크롤 게시글 카테고리별 조회
    @Transactional(readOnly = true)
    public ResponseEntity<?> categoryGetPosts(int page, int size, String category) {
        User user = SecurityUtil.getCurrentUser();
        Pageable pageable = PageRequest.of(page, size);
        PostCondition postCondition = new PostCondition();
        postCondition.setCategory(category);
        Slice<PostResponseDto> postResponseDtos = postRepositoryImpl.dynamicScrollPost(pageable, user, postCondition);
        return new ResponseEntity<>(postResponseDtos, HttpStatus.OK);
    }


    //상세조회
    @Transactional(readOnly = true)
    public ResponseEntity<?> getPost(Long postId) {
        User user = SecurityUtil.getCurrentUser();
        PostResponseDto post = postRepositoryImpl.findPostById(user, postId);
        return new ResponseEntity<>(post, HttpStatus.OK);
    }
    //수정
    @Transactional
    public ResponseEntity<?> updatePost(Long postId, PostRequestDto requestDto) throws IOException {
        User user = SecurityUtil.getCurrentUser();

        if(requestDto.getTitle().length()>50) {
            throw new RestApiException(CommonStatusCode.OVER_TITLE);
        }
        if(requestDto.getContent().length()>500) {
            throw new RestApiException(CommonStatusCode.OVER_CONTENT);
        }

        Post post = postRepository.findById(postId).orElseThrow(
                ()-> new RestApiException(CommonStatusCode.NO_ARTICLE)
        );
        if(!post.getUser().getUserId().equals(user.getUserId())){
            throw new RestApiException(CommonStatusCode.INVALID_USER_UPDATE);
        }
        String imgUrl = null;
        if(requestDto.getFile()!=null){
            imgUrl = s3Uploader.upload(requestDto.getFile(), "files");
        }
        if(imgUrl==null && requestDto.getIsDelete().equals("true")) {
            post.update(requestDto, imgUrl);
        } else if(imgUrl==null) {
            post.contentUpdate(requestDto);
        } else {
            post.update(requestDto, imgUrl);
        }

        boolean isLike = likesRepository.existsByUserIdAndPostId(user.getId(), post.getId());
        Long likeCount = likesRepository.countByPostId(post.getId());
        Long commentCount = commentRepository.countByPostId(post.getId());
        return new ResponseEntity<>(new PostResponseDto(post, isLike, likeCount, commentCount), HttpStatus.OK);
    }
    //삭제
    @Transactional
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
        User user = SecurityUtil.getCurrentUser();
        Pageable pageable = PageRequest.of(page,size);
        PostCondition postCondition = new PostCondition();
        postCondition.setKeyword(keyword.getKeyword());
        Page<PostResponseDto> postPage = postRepositoryImpl.dynamicPagePost(pageable, user, postCondition);

        return new ResponseEntity<>(postPage, HttpStatus.OK);
    }

}

