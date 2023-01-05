package com.example.alba_pocket.entity;

import com.example.alba_pocket.dto.WorkPlaceRequestDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@RequiredArgsConstructor
public class WorkPlace {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String placeName;

    @Column
    private String placeColor;

    @Column
    private int salaryDay;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    public WorkPlace(User user, WorkPlaceRequestDto requestDto) {
        this.user = user;
        this.placeName = requestDto.getPlaceName();
        this.placeColor = requestDto.getPlaceColor();
        this.salaryDay = requestDto.getSalaryDay();
    }
}
