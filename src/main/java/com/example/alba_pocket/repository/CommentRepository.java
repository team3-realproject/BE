package com.example.alba_pocket.repository;

import com.example.alba_pocket.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

}