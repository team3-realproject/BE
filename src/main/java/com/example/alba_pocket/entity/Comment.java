package com.example.alba_pocket.entity;

import com.example.alba_pocket.dto.CommentRequestDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@RequiredArgsConstructor
public class Comment extends Timestamped{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String comment;

    @Column
    private Long userId;

    @Column
    private Long postId;

    @Transient
    private String nickname;


    public Comment(User user, Long postId, CommentRequestDto requestDto) {
        this.userId = user.getId();
        this.postId = postId;
        this.comment = requestDto.getComment();
        this.nickname = user.getNickname();
    }

    public void update(CommentRequestDto requestDto, User user) {
        this.comment = requestDto.getComment();
        this.nickname = user.getNickname();
    }
}
