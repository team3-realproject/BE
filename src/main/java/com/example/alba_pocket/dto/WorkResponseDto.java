package com.example.alba_pocket.dto;

import com.example.alba_pocket.entity.Work;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class WorkResponseDto {

    private Long placeId;
    private String placeName;
    private String placeColor;
    private int salaryDay;

    public WorkResponseDto(Work work) {
        this.placeId = work.getId();
        this.placeName = work.getPlaceName();
        this.placeColor = work.getPlaceColor();
        this.salaryDay = work.getSalaryDay();
    }
}
