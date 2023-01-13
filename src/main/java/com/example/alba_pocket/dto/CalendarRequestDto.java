package com.example.alba_pocket.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class CalendarRequestDto {

    @JsonFormat(pattern = "HH:mm")
    private LocalTime startTime;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime endTime;
    private List<CalendarRequestDto.WorkDay> workDay;
    private int year;
    private int month;
    private int date;
    private int hourlyWage;

    @Getter
    @NoArgsConstructor
    public static class WorkDay {
        private String workday;
    }



}
