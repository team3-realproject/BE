package com.example.alba_pocket.dto;

import com.example.alba_pocket.entity.Calendar;
import com.example.alba_pocket.entity.WorkPlace;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@NoArgsConstructor
public class CalendarResponseDto {
    private Long todoId;
    private int year;
    private int month;
    private int date;
    //근무지명
    private String placeName;
    //컬러
    private String color;

    //근무시간
    @JsonFormat(pattern = "HH:mm")
    private LocalTime workingTime;

    //시작시간
    @JsonFormat(pattern = "HH:mm")
    private LocalTime startTime;

    //끝나는시간
    @JsonFormat(pattern = "HH:mm")
    private LocalTime endTime;
    //시급
    private int hourlyWage;
    //정산금액
    private int dayWage;

    public CalendarResponseDto(Calendar calendar, int pay) {
        this.todoId = calendar.getId();
        this.placeName = calendar.getWorkPlace().getPlaceName();
        this.color = calendar.getWorkPlace().getPlaceColor();
        this.workingTime = calendar.getWorkingTime();
        this.startTime = calendar.getStartTime();
        this.endTime = calendar.getEndTime();
        this.hourlyWage = calendar.getHourlyWage();
        this.dayWage = pay;
        this.year = calendar.getWorkDay().getYear();
        this.month = calendar.getWorkDay().getMonthValue();
        this.date = calendar.getWorkDay().getDayOfMonth();
    }

    @Getter
    @NoArgsConstructor
    public static class BonusResponseDto {
        private String bonusName;
        private int year;
        private int month;
        private int date;
        private int bonus;

        private String color;
        public BonusResponseDto(int bonus, LocalDate sunday, WorkPlace workPlace) {
            this.bonusName = "주휴수당(" + workPlace.getPlaceName() + ")";
            this.year = sunday.getYear();
            this.month = sunday.getMonthValue();
            this.date = sunday.getDayOfMonth();
            this.bonus = bonus;
            this.color = workPlace.getPlaceColor();
        }
    }

    @Getter
    @NoArgsConstructor
    public static class StatutoryLeisurePayResponseDto {
        private boolean result;
        private LocalTime totalTime;
        private LocalDate sunday;
        public StatutoryLeisurePayResponseDto(boolean result, LocalTime local, LocalDate sun) {
            this.result = result;
            this.totalTime = local;
            this.sunday = sun;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class totalPayResponseDto {
        private int year;
        private int month;
        private int total;

        public totalPayResponseDto(int totalPay, LocalDate early) {
            this.year = early.getYear();
            this.month = early.getMonthValue();
            this.total = totalPay;
        }
    }



}
