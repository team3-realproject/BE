package com.example.alba_pocket.entity;

import com.example.alba_pocket.dto.CommentRequestDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@RequiredArgsConstructor
public class Comment extends Timestamped{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100)
    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Transient
    private String nickname;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "post_id")
    private Post post;

    @OneToMany(mappedBy = "comment", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    @OrderBy("id asc")
    List<CommentLikes> commentLikes = new ArrayList<>();




    public Comment(User user, Post post, CommentRequestDto requestDto) {
        this.user = user;
        this.post = post;
        this.comment = requestDto.getComment();
        this.nickname = user.getNickname();
    }

    public void update(CommentRequestDto requestDto, User user) {
        this.comment = requestDto.getComment();
        this.nickname = user.getNickname();
    }
}
