package com.example.alba_pocket.repository;

import com.example.alba_pocket.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;



public interface PostRepository extends JpaRepository<Post, Long> {

}
