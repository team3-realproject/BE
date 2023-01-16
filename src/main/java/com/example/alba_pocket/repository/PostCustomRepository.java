package com.example.alba_pocket.repository;


import com.example.alba_pocket.entity.Post;
import com.example.alba_pocket.model.PostSearchKeyword;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostCustomRepository {
//    List<Post> search(PostSearchKeyword keyword);
    Page<Post> searchPage(PostSearchKeyword keyword, Pageable pageable);
}
