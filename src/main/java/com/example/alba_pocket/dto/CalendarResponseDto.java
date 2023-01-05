package com.example.alba_pocket.dto;

import com.example.alba_pocket.entity.Calendar;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class CalendarResponseDto {
    //근무지명
    private String placeName;
    //컬러
    private String placeColor;
    //근무시간
//    @JsonFormat(pattern = "HH:mm")
    private int time;
    //근무날짜
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate workDay;
    //시급
    private int payOrigin;
    //정산금액
    private int pay;

    public CalendarResponseDto(Calendar calendar, int pay) {
        this.placeName = calendar.getWorkPlace().getPlaceName();
        this.placeColor = calendar.getWorkPlace().getPlaceColor();
        this.time = calendar.getTime();
        this.workDay = calendar.getWorkDay();
        this.payOrigin = calendar.getPayOrigin();
        this.pay = pay;
    }
}
