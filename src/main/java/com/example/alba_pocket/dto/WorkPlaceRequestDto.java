package com.example.alba_pocket.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
@Getter
@NoArgsConstructor
public class WorkPlaceRequestDto {

    private String placeName;

//    @JsonFormat(pattern = "dd")
    private int salaryDay;

    private String placeColor;
}
