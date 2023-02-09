package com.example.alba_pocket.repository;


import com.example.alba_pocket.dto.PostCondition;
import com.example.alba_pocket.dto.PostResponseDto;
import com.example.alba_pocket.entity.User;
import com.example.alba_pocket.model.PostSearchKeyword;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface PostCustomRepository {

    Slice<PostResponseDto> dynamicScrollPost(Pageable pageable, User user, PostCondition postCondition);
    Page<PostResponseDto> dynamicPagePost(Pageable pageable, User user, PostCondition postCondition);
    Page<PostResponseDto> myLikePostPage(User user, Pageable pageable);
    PostResponseDto findPostById(User user, Long postId);

}
