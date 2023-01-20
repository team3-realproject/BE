package com.example.alba_pocket.entity;

import com.example.alba_pocket.dto.WorkRequestDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@RequiredArgsConstructor
public class Work {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column (nullable = false)
    String placeName;

    @Column (nullable = false)
    String placeColor;

    @Column
    int salaryDay;

    @ManyToOne(fetch = FetchType.LAZY)
    User user;

    @OneToMany(mappedBy = "work", cascade = CascadeType.REMOVE)
    List<Calendar> calendar = new ArrayList<>();


    public Work(WorkRequestDto workRequestDto, User user) {
        this.placeName = workRequestDto.getPlaceName();
        this.placeColor = workRequestDto.getPlaceColor();
        this.salaryDay = workRequestDto.getSalaryDay();
        this.user = user;
    }


    public void updateWorkPlace(WorkRequestDto workRequestDto) {
        this.placeName = workRequestDto.getPlaceName();
        this.placeColor = workRequestDto.getPlaceColor();
        this.salaryDay = workRequestDto.getSalaryDay();
    }
}
