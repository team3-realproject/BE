package com.example.alba_pocket.service;

import com.example.alba_pocket.dto.CalendarRequestDto;
import com.example.alba_pocket.dto.CalendarResponseDto;
import com.example.alba_pocket.dto.StatutoryLeisurePayResponseDto;
import com.example.alba_pocket.entity.Calendar;
import com.example.alba_pocket.entity.User;
import com.example.alba_pocket.entity.WorkPlace;
import com.example.alba_pocket.repository.CalendarRepository;
import com.example.alba_pocket.repository.WorkPlaceRepository;
import com.example.alba_pocket.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CalendarService {

    private final WorkPlaceRepository workPlaceRepository;

    private final CalendarRepository calendarRepository;

    @Transactional
    public ResponseEntity<?> createCalendar(Long workplaceId, CalendarRequestDto requestDto) {
        User user = SecurityUtil.getCurrentUser();
        WorkPlace workPlace = workPlaceRepository.findById(workplaceId).orElseThrow(
                () -> new IllegalArgumentException("알바정보없음")
        );
        if (!user.getId().equals(workPlace.getUser().getId())) {
            throw new IllegalArgumentException("본인아님");
        }
        LocalTime time = requestDto.getTime();
        int payOrigin = requestDto.getPayOrigin();
        int pay = Pay(time, payOrigin);
        Calendar calendar = new Calendar(workPlace, requestDto, user);
        calendarRepository.saveAndFlush(calendar);

        return new ResponseEntity<>(new CalendarResponseDto(calendar, pay), HttpStatus.OK);
    }

    @Transactional
    public int Pay(LocalTime time, int payOrigin) {
        int hour = Integer.parseInt(time.format(DateTimeFormatter.ofPattern("HH")));
        int minute = Integer.parseInt(time.format(DateTimeFormatter.ofPattern("mm")));
        log.info("시간단위 ------------------------" + String.valueOf(hour));
        log.info("분단위 ------------------------" + String.valueOf(minute));
        int a = 60 / minute;
        int b = payOrigin / a;
//        LocalDate today = LocalDate.parse("2023-01-06");
//        var startWeekDay = today.with(DayOfWeek.MONDAY);
//        var dates = startWeekDay.datesUntil(startWeekDay.plusWeeks(1)).collect(Collectors.toList());
//        AtomicReference<LocalTime> totalTime = new AtomicReference<>(LocalTime.parse(LocalTime.parse("00:00").format(DateTimeFormatter.ofPattern("HH:mm"))));
//        dates.stream().map(date -> {
//            Calendar calendar = calendarRepository.findByWorkDayAndAndWorkPlaceId(date, workplaceId).orElse(new Calendar());
//            if(calendar.getTime() != null) {
//                totalTime.set(totalTime.get().plusHours(calendar.getTime().getHour()).plusMinutes(calendar.getTime().getMinute()));
//            }
//            return totalTime;
//        }).collect(Collectors.toList());
//        LocalTime local = LocalTime.parse(totalTime.toString());
//
//        if(local.equals(LocalTime.parse(LocalTime.parse("15:00").format(DateTimeFormatter.ofPattern("HH:mm")))) || local.isAfter(LocalTime.parse(LocalTime.parse("15:00").format(DateTimeFormatter.ofPattern("HH:mm"))))){
//            System.out.println("주휴수당!!!");
//        }

        return (hour * payOrigin) + b;
    }

    public StatutoryLeisurePayResponseDto StatutoryLeisurePayTime(Long workplaceId) {

        LocalDate today = LocalDate.parse("2023-01-06");
        var startWeekDay = today.with(DayOfWeek.MONDAY);
        var dates = startWeekDay.datesUntil(startWeekDay.plusWeeks(1)).collect(Collectors.toList());
        AtomicReference<LocalTime> totalTime = new AtomicReference<>(LocalTime.parse(LocalTime.parse("00:00").format(DateTimeFormatter.ofPattern("HH:mm"))));
        dates.stream().map(date -> {
            Calendar calendar = calendarRepository.findByWorkDayAndAndWorkPlaceId(date, workplaceId).orElse(new Calendar());
            if (calendar.getTime() != null) {
                totalTime.set(totalTime.get().plusHours(calendar.getTime().getHour()).plusMinutes(calendar.getTime().getMinute()));
            }
            return totalTime;
        }).collect(Collectors.toList());
        LocalTime local = LocalTime.parse(totalTime.toString());
        boolean result = local.equals(LocalTime.parse(LocalTime.parse("15:00").format(DateTimeFormatter.ofPattern("HH:mm")))) || local.isAfter(LocalTime.parse(LocalTime.parse("15:00").format(DateTimeFormatter.ofPattern("HH:mm"))));
        return new StatutoryLeisurePayResponseDto(result, local);
    }

    public int StatutoryLeisurePay(LocalTime totalTime, double payOrigin) {
        int hour = Integer.parseInt(totalTime.format(DateTimeFormatter.ofPattern("HH")));
        double minute = Integer.parseInt(totalTime.format(DateTimeFormatter.ofPattern("mm"))) / 60;
        double total = hour + minute;
        log.info("토탈시간----" + total);


        return (int) (total * payOrigin) / 5;
    }

    @Transactional(readOnly = true)
    public ResponseEntity<?> getCalendar() {
        User user = SecurityUtil.getCurrentUser();
        List<Calendar> calendars = calendarRepository.findAllByUserId(user.getId());
        List<WorkPlace> workPlaces = workPlaceRepository.findAllByUserId(user.getId());

        workPlaces.forEach(workPlace -> {
            StatutoryLeisurePayResponseDto statutoryLeisurePay = StatutoryLeisurePayTime(workPlace.getId());
            if (statutoryLeisurePay.isResult()) {
                AtomicInteger Origin = new AtomicInteger();
                List<Calendar> calendarList = calendarRepository.findAllByUserIdAndAndWorkPlaceId(user.getId(), workPlace.getId());
                calendarList.stream().forEach(calendar -> {
                    Origin.addAndGet(calendar.getPayOrigin());
                });
                double payOrigin = Integer.parseInt(String.valueOf(Origin)) / calendarList.size();
                System.out.println("----------------------" + payOrigin);
                log.info("true일때 주휴수당 o ---------------------------------------------------");
                int i = StatutoryLeisurePay(statutoryLeisurePay.getLocalTime(), payOrigin);
                log.info("총주휴수당--------------------------------------------" + i);
            }
        });

        return new ResponseEntity<>(calendars.stream().map(calendar -> {
            int pay = Pay(calendar.getTime(), calendar.getPayOrigin());
            return new CalendarResponseDto(calendar, pay);
        }).collect(Collectors.toList()), HttpStatus.OK);
    }


//    public boolean isAfter(LocalTime other) {
//        return other.isAfter(LocalTime.parse(LocalTime.parse("15:00").format(DateTimeFormatter.ofPattern("HH:mm"))));
//    }


}
