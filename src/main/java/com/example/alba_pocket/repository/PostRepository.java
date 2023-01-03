package com.example.alba_pocket.repository;

import com.example.alba_pocket.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByOrderByCreatedAt();

    //게시글 검색
    List<Post> findAllByTitleContainingOrContentContainingOrderByCreatedAtDesc(String keyword, String keyword1);

}
