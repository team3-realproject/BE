package com.example.alba_pocket.dto;

import com.example.alba_pocket.entity.WorkPlace;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class WorkPlaceResponseDto {
    private Long placeId;

    private String placeName;


    private int salaryDay;

    private String placeColor;


    public WorkPlaceResponseDto(WorkPlace workPlace) {
        this.placeId = workPlace.getId();
        this.placeName = workPlace.getPlaceName();
        this.salaryDay = workPlace.getSalaryDay();
        this.placeColor = workPlace.getPlaceColor();
    }


}
