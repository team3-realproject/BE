package com.example.alba_pocket.repository;

import com.example.alba_pocket.entity.Comment;
import com.example.alba_pocket.entity.Likes;
import com.example.alba_pocket.entity.Post;
import com.example.alba_pocket.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikesRepository extends JpaRepository<Likes, Long> {
    Optional<Likes> findByUserIdAndPostId(Long userId, Long postId);

    boolean existsByUserIdAndPostId(Long userId, Long postId);

    int countByPostId(Long postId);

}
