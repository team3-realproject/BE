package com.example.alba_pocket.entity;

import com.example.alba_pocket.dto.PostRequestDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class Post extends Timestamped{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Column
    private String content;

    @Column
    private String imgUrl;

    @Column
    private String category;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    public Post(Post posts, User user, String imgUrl) {
        this.title = posts.getTitle();
        this.content = posts.getContent();
        this.imgUrl = imgUrl;
        this.category = posts.getCategory();
        this.user = user;
    }




    public void update(PostRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.category = requestDto.getCategory();
    }
}