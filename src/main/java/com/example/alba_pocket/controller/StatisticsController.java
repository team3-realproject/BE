package com.example.alba_pocket.controller;

import com.example.alba_pocket.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/statistics")
public class StatisticsController {
    private final StatisticsService statisticsService;

    @GetMapping("/time/{month}")
    public ResponseEntity<?> getStatisticsTime(@PathVariable String month) {
        return statisticsService.getStatisticsTime(month);
    }

    @GetMapping("/fivemonth")
    public ResponseEntity<?> getStatisticsPay() {
        return statisticsService.getStatisticsPay();
    }
}
