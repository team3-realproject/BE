package com.example.alba_pocket.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@RequiredArgsConstructor
public class CommentLikes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id", nullable = false)
    private Comment comment;

    @Column
    private boolean isLikePost;

    public CommentLikes(User user, Comment comment) {
        this.userId = user.getId();
        this.comment = comment;
        this.isLikePost = true;
    }
}
