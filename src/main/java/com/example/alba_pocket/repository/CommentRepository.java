package com.example.alba_pocket.repository;

import com.example.alba_pocket.entity.Comment;
import com.example.alba_pocket.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByPostIdOrderByCreatedAtAsc(Long postId);
    int countByPostId(Long postId);

}