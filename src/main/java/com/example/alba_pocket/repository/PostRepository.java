package com.example.alba_pocket.repository;

import com.example.alba_pocket.entity.Post;
import com.example.alba_pocket.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByOrderByCreatedAtDesc();
    List<Post> findByUserOrderByCreatedAt(User user);

    List<Post> findAllByCategoryOrderByCreatedAtDesc(String category);


    //게시글 검색
    List<Post> findAllByTitleContainingOrContentContainingOrderByCreatedAtDesc(String keyword, String keyword1);

}
