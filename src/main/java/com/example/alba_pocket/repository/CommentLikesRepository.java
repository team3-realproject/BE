package com.example.alba_pocket.repository;

import com.example.alba_pocket.entity.CommentLikes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentLikesRepository extends JpaRepository<CommentLikes, Long> {
    Optional<CommentLikes> findByUserIdAndCommentId(Long userId, Long commentId);
}
