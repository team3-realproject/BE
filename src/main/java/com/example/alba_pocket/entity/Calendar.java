package com.example.alba_pocket.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@RequiredArgsConstructor
public class Calendar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //일한시간
    @Column
    private int time;
    //일한날짜
    @Column
    private Date workDate;
    //시급
    private int pay;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
}
