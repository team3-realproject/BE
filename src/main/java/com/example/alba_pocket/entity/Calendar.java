package com.example.alba_pocket.entity;

import com.example.alba_pocket.dto.CalendarRequestDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@RequiredArgsConstructor
public class Calendar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //일한시간
    @Column
//    @JsonFormat(pattern = "HH:mm")
    private int time;
    //일한날짜
    @Column
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate workDay;
    //시급
    private int payOrigin;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private WorkPlace workPlace;

    public Calendar(WorkPlace workPlace, CalendarRequestDto requestDto, User user) {
        this.time = requestDto.getTime();
        this.workDay = requestDto.getWorkDay();
        this.payOrigin = requestDto.getPayOrigin();
        this.user = user;
        this.workPlace = workPlace;
    }
}
