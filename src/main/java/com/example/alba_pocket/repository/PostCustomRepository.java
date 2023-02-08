package com.example.alba_pocket.repository;


import com.example.alba_pocket.dto.MyPageRequestDto;
import com.example.alba_pocket.dto.PostResponseDto;
import com.example.alba_pocket.entity.User;
import com.example.alba_pocket.model.PostSearchKeyword;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface PostCustomRepository {

    Slice<PostResponseDto> scrollPost(Pageable pageable, User user);

    Slice<PostResponseDto> scrollCategoryPost(Pageable pageable, User user, String category);

    Page<PostResponseDto> searchPage(PostSearchKeyword keyword, Pageable pageable, User user);

    Page<PostResponseDto> myLikePostPage(User user, Pageable pageable);

}
