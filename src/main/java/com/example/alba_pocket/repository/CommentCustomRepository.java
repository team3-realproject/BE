package com.example.alba_pocket.repository;

import com.example.alba_pocket.dto.CommentResponseDto;
import com.example.alba_pocket.dto.MypageCommentResponseDto;
import com.example.alba_pocket.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CommentCustomRepository {
    List<CommentResponseDto> commentList(User user, Long postId);
    Page<MypageCommentResponseDto> myCommentPostPage(User user, Pageable pageable);
}
