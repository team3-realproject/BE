package com.example.alba_pocket.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@RequiredArgsConstructor
public class Likes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long userId;

    @Column
    private Long postId;

    @Column
    private boolean isLikePost;

    public Likes(User user, Post post) {
        this.userId = user.getId();
        this.postId = post.getId();
        this.isLikePost = true;
    }
}
