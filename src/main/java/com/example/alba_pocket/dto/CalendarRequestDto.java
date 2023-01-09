package com.example.alba_pocket.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@NoArgsConstructor
public class CalendarRequestDto {

    @JsonFormat(pattern = "HH:mm")
    private LocalTime time;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate workDay;

    private int payOrigin;


}
