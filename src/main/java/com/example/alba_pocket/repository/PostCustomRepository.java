package com.example.alba_pocket.repository;


import com.example.alba_pocket.dto.PostResponseDto;
import com.example.alba_pocket.entity.Post;
import com.example.alba_pocket.entity.User;
import com.example.alba_pocket.model.PostSearchKeyword;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface PostCustomRepository {
    List<Post> search(PostSearchKeyword keyword);
    Slice<PostResponseDto> scrollPost(Pageable pageable, User user);

}
