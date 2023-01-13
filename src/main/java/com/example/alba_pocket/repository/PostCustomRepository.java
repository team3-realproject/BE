package com.example.alba_pocket.repository;


import com.example.alba_pocket.entity.Post;
import com.example.alba_pocket.model.PostSearchKeyword;

import java.util.List;

public interface PostCustomRepository {
    List<Post> search(PostSearchKeyword keyword);

}
