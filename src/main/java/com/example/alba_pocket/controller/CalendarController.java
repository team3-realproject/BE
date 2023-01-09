package com.example.alba_pocket.controller;

import com.example.alba_pocket.dto.CalendarRequestDto;
import com.example.alba_pocket.service.CalendarService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/calendar")
@RequiredArgsConstructor
public class CalendarController {

    private final CalendarService calendarService;

    @PostMapping("/{workplaceId}")
    public ResponseEntity<?> createCalendar(@PathVariable Long workplaceId, @RequestBody CalendarRequestDto requestDto){
        return calendarService.createCalendar(workplaceId, requestDto);
    }

    @GetMapping
    public ResponseEntity<?> getCalendar(){
        return calendarService.getCalendar();
    }

    @GetMapping("/wngb")
    public ResponseEntity<?> wngb(){
        return null;
    }

}
