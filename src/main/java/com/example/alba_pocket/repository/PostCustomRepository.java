package com.example.alba_pocket.repository;

import com.example.alba_pocket.dto.PostReadResponseDto;
import com.example.alba_pocket.model.PostSearchKeyword;

import java.util.List;

public interface PostCustomRepository {
    List<PostReadResponseDto> search(PostSearchKeyword condition);
}
