package com.example.alba_pocket.controller;

import com.example.alba_pocket.dto.CalendarRequestDto;
import com.example.alba_pocket.service.CalendarService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CalendarController {

    private final CalendarService calendarService;
    //근무등록
    @PostMapping("/work/{placeId}")
    public ResponseEntity<?> createCalendar(@PathVariable Long placeId, @RequestBody CalendarRequestDto requestDto){
        return calendarService.createCalendar(placeId, requestDto);
    }
    //근무달력조회(월별)
    @GetMapping("/calendar/month/{month}")
    public ResponseEntity<?> getCalendar(@PathVariable String month){
        return calendarService.getCalendar(month);
    }
    //근무달력조회(일별)
    @GetMapping("/calendar/day/{day}")
    public ResponseEntity<?> dayCalendar(@PathVariable String day){
        return calendarService.dayCalendar(day);
    }
    //주휴수당
    @GetMapping("/calendar/bonus/{month}")
    public ResponseEntity<?> bonus(@PathVariable String month){
        return calendarService.bonus(month);
    }
    //주휴수당 일별겟요청
    @GetMapping("/calendar/day/bonus/{day}")
    public ResponseEntity<?> dayBonus(@PathVariable String day){
        return calendarService.dayBonus(day);
    }
    //월 총합 급여
    @GetMapping("/calendar/total/{month}")
    public ResponseEntity<?> getTotalPay(@PathVariable String month){
        return calendarService.getTotalPay(month);
    }

    //근무일삭제
    @DeleteMapping("/work/{todoId}")
    public ResponseEntity<?> deleteDay(@PathVariable Long todoId){
        return calendarService.deleteDay(todoId);
    }





}
