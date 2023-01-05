package com.example.alba_pocket.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class CalendarRequestDto {

//    @JsonFormat(pattern = "HH:mm")
    private int time;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate workDay;

    private int payOrigin;


}
