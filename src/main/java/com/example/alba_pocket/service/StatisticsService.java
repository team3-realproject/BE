package com.example.alba_pocket.service;

import com.example.alba_pocket.dto.StatisticsMonthResponseDto;
import com.example.alba_pocket.dto.StatisticsResponseDto;
import com.example.alba_pocket.entity.Calendar;
import com.example.alba_pocket.entity.User;
import com.example.alba_pocket.entity.Work;
import com.example.alba_pocket.repository.CalendarRepository;
import com.example.alba_pocket.repository.WorkRepository;
import com.example.alba_pocket.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Slf4j
@RequiredArgsConstructor
public class StatisticsService {
    private final CalendarRepository calendarRepository;
    private final WorkRepository workRepository;

    @Transactional
    public ResponseEntity<?> getStatisticsTime(String month) {
        User user = SecurityUtil.getCurrentUser();
        List<Work> workList = workRepository.findByUser(user);

        StatisticsResponseDto statisticsResponseDto = new StatisticsResponseDto();
        month += "01";
        for (Work work : workList) {
            LocalDate startDay = LocalDate.parse(month, DateTimeFormatter.ofPattern("yyyyMMdd"));
            LocalDate endDay = startDay.withDayOfMonth(startDay.lengthOfMonth());
            List<Calendar> calendars = calendarRepository.workTotalPay(user.getId(), work.getId(), startDay, endDay);

            int hour = 0;
            int minute = 0;
            for(Calendar calendar : calendars) {
                hour += Integer.parseInt(calendar.getWorkingTime().format(DateTimeFormatter.ofPattern("HH")));
                minute += Integer.parseInt(calendar.getWorkingTime().format(DateTimeFormatter.ofPattern("mm")));
            }
            hour += minute/60;

            statisticsResponseDto.addLabels(work.getPlaceName());
            statisticsResponseDto.addColors(work.getPlaceColor());
            statisticsResponseDto.addSeries(hour);
        }
        return new ResponseEntity<>(statisticsResponseDto, HttpStatus.OK);

    }

    @Transactional
    public ResponseEntity<?> getStatisticsPay() {
        User user = SecurityUtil.getCurrentUser();
        List<Work> workList = workRepository.findByUser(user);

        StatisticsMonthResponseDto statisticsMonthResponseDto = new StatisticsMonthResponseDto();
        List<LocalDate> earlys = new ArrayList<>();
        for (int i = 4; i >= 0; i--) {
            earlys.add(LocalDate.now().minusMonths(i));
            statisticsMonthResponseDto.addCategory(LocalDate.now().minusMonths(i).getYear()+"-"+LocalDate.now().minusMonths(i).getMonthValue());
        }

        for (Work work : workList) {
            StatisticsMonthResponseDto.PlaceFiveMonthDto placeFiveMonthDto = new StatisticsMonthResponseDto.PlaceFiveMonthDto(work.getPlaceName());
            statisticsMonthResponseDto.addColor(work.getPlaceColor());
            for (LocalDate early : earlys) {
                LocalDate startDay = early.withDayOfMonth(1);
                LocalDate endDay = early.withDayOfMonth(early.lengthOfMonth());

                List<Calendar> calendars = calendarRepository.workTotalPay(user.getId(), work.getId(), startDay, endDay);

                System.out.println(calendars.size());
                AtomicInteger total = new AtomicInteger();
                calendars.forEach(calendar -> {
                    total.addAndGet(Pay(calendar.getWorkingTime(), calendar.getHourlyWage()));
                });
                int totalWage = Integer.parseInt(String.valueOf(total));
                placeFiveMonthDto.addData(totalWage);
            }
            statisticsMonthResponseDto.addSeries(placeFiveMonthDto);

        }
        return new ResponseEntity<>(statisticsMonthResponseDto, HttpStatus.OK);
    }

    @Transactional
    public int Pay(LocalTime workingTime, int hourlyWage) {
        int hour = Integer.parseInt(workingTime.format(DateTimeFormatter.ofPattern("HH")));
        int minute = Integer.parseInt(workingTime.format(DateTimeFormatter.ofPattern("mm")));
        double calculateMinutes;
        try {
            calculateMinutes = hourlyWage / (60.0 / minute);
        } catch (ArithmeticException e) {
            log.info("분단위가0");
            calculateMinutes = 0;
        }
        return (hour * hourlyWage) + (int) Math.round(calculateMinutes);
    }

}
