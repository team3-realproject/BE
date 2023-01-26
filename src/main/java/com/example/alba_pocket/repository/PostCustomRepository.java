package com.example.alba_pocket.repository;


import com.example.alba_pocket.dto.MypageCommentResponseDto;
import com.example.alba_pocket.dto.PostResponseDto;
import com.example.alba_pocket.entity.Post;
import com.example.alba_pocket.entity.User;
import com.example.alba_pocket.model.PostSearchKeyword;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Page;



import java.util.List;

public interface PostCustomRepository {

    Slice<PostResponseDto> scrollPost(Pageable pageable, User user);

    Slice<PostResponseDto> scrollCategoryPost(Pageable pageable, User user, String category);

    Page<PostResponseDto> searchPage(PostSearchKeyword keyword, Pageable pageable, User user);

    Page<PostResponseDto> myLikePostPage(User user, Pageable pageable);
    Page<MypageCommentResponseDto> myCommentPostPage(User user, Pageable pageable);

}
