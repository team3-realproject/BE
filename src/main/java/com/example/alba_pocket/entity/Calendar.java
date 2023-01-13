package com.example.alba_pocket.entity;

import com.example.alba_pocket.dto.CalendarRequestDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@RequiredArgsConstructor
public class Calendar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //일한시간
    @Column
    @JsonFormat(pattern = "HH:mm")
    private LocalTime workingTime;
    @Column
    @JsonFormat(pattern = "HH:mm")
    private LocalTime startTime;
    @Column
    @JsonFormat(pattern = "HH:mm")
    private LocalTime endTime;
    //일한날짜
    @Column
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate workDay;
    //시급
    private int hourlyWage;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private WorkPlace workPlace;

    public Calendar(WorkPlace workPlace, CalendarRequestDto requestDto, User user, LocalTime workingTime, LocalDate workDay) {
        this.workingTime = workingTime;
        this.startTime = requestDto.getStartTime();
        this.endTime = requestDto.getEndTime();
        this.workDay = workDay;
        this.hourlyWage = requestDto.getHourlyWage();
        this.user = user;
        this.workPlace = workPlace;
    }
}
