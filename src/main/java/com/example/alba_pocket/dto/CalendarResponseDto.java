package com.example.alba_pocket.dto;

import com.example.alba_pocket.entity.Calendar;
import com.example.alba_pocket.entity.Work;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class CalendarResponseDto {
    private Long todoId;
    private String year;
    private String month;
    private String date;
    //근무지명
    private String placeName;
    //컬러
    private String color;

    //근무시간
    private String workingTime;

    //시작시간
    private String startTime;

    //끝나는시간
    @JsonFormat(pattern = "HH:mm")
    private String endTime;
    //시급
    private String hourlyWage;
    //정산금액
    private String dayWage;

    public CalendarResponseDto(Calendar calendar, int pay) {
        this.todoId = calendar.getId();
        this.placeName = calendar.getWork().getPlaceName();
        this.color = calendar.getWork().getPlaceColor();
        this.workingTime = String.valueOf(calendar.getWorkingTime());
        this.startTime = String.valueOf(calendar.getStartTime());
        this.endTime = String.valueOf(calendar.getEndTime());
        this.hourlyWage = String.valueOf(calendar.getHourlyWage());
        this.dayWage = String.valueOf(pay);
        this.year = String.valueOf(calendar.getWorkDay().getYear());
        this.month = String.valueOf(calendar.getWorkDay().getMonthValue()).length() == 1 ? "0" + calendar.getWorkDay().getMonthValue() : String.valueOf(calendar.getWorkDay().getMonthValue());
        this.date = String.valueOf(calendar.getWorkDay().getDayOfMonth()).length() == 1 ? "0" + calendar.getWorkDay().getDayOfMonth() : String.valueOf(calendar.getWorkDay().getDayOfMonth());

    }

    @Getter
    @NoArgsConstructor
    public static class BonusResponseDto {
        private String bonusName;
        private String year;
        private String month;
        private String date;
        private String bonus;

        private String color;

        public BonusResponseDto(int bonus, LocalDate sunday, Work work) {
            this.bonusName = "주휴수당(" + work.getPlaceName() + ")";
            this.year = String.valueOf(sunday.getYear());
            this.month = String.valueOf(sunday.getMonthValue()).length() == 1 ? "0" + sunday.getMonthValue() : String.valueOf(sunday.getMonthValue());
            this.date = String.valueOf(sunday.getDayOfMonth()).length() == 1 ? "0" + sunday.getDayOfMonth() : String.valueOf(sunday.getDayOfMonth());
            this.bonus = String.valueOf(bonus);
            this.color = work.getPlaceColor();
        }
    }

    @Getter
    @NoArgsConstructor
    public static class StatutoryLeisurePayResponseDto {
        private boolean result;
        private int hour;
        private int minute;
        private LocalDate sunday;

        public StatutoryLeisurePayResponseDto(boolean result, int hour, int minute, LocalDate sunday) {
            this.result = result;
            this.hour = hour;
            this.minute = minute;
            this.sunday = sunday;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class totalPayResponseDto {
        private String year;
        private String month;
        private String total;

        public totalPayResponseDto(int totalPay, LocalDate early) {
            this.year = String.valueOf(early.getYear());
            this.month = String.valueOf(early.getMonthValue()).length() == 1 ? "0" + early.getMonthValue() : String.valueOf(early.getMonthValue());
            this.total = String.valueOf(totalPay);
        }
    }


}
