package com.example.alba_pocket.service;

import com.example.alba_pocket.dto.CalendarRequestDto;
import com.example.alba_pocket.dto.CalendarResponseDto;
import com.example.alba_pocket.dto.MsgResponseDto;
import com.example.alba_pocket.entity.Calendar;
import com.example.alba_pocket.entity.User;
import com.example.alba_pocket.entity.WorkPlace;
import com.example.alba_pocket.errorcode.CommonStatusCode;
import com.example.alba_pocket.exception.RestApiException;
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
import java.util.ArrayList;
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

    //근무등록
    @Transactional
    public ResponseEntity<?> createCalendar(Long placeId, CalendarRequestDto requestDto) {
        User user = SecurityUtil.getCurrentUser();
        WorkPlace workPlace = workPlaceRepository.findById(placeId).orElseThrow(
                () -> new IllegalArgumentException("알바정보없음")
        );
        if (!user.getId().equals(workPlace.getUser().getId())) {
            throw new IllegalArgumentException("본인아님");
        }
        requestDto.getWorkDay().forEach(workDay -> {
            LocalDate workDate = LocalDate.parse(workDay.getWorkday(), DateTimeFormatter.ofPattern("yyyyMMdd"));
            LocalTime workingTime = requestDto.getEndTime().minusHours(requestDto.getStartTime().getHour()).minusMinutes(requestDto.getStartTime().getMinute());
            Calendar calendar = new Calendar(workPlace, requestDto, user, workingTime, workDate);
            calendarRepository.saveAndFlush(calendar);
        });
        return new ResponseEntity<>(new MsgResponseDto("근무등록완료"), HttpStatus.OK);
    }

    //근무달력조회(월별)
    @Transactional(readOnly = true)
    public ResponseEntity<?> getCalendar(String month) {
        User user = SecurityUtil.getCurrentUser();
        log.info(month);
        month += "01";
        LocalDate early = LocalDate.parse(month, DateTimeFormatter.ofPattern("yyyyMMdd"));
        LocalDate endDay = early.withDayOfMonth(early.lengthOfMonth());
        log.info("월초 - " + early + "----" + "월말 - " + endDay);
        List<Calendar> calendars = calendarRepository.findByUserIdAndWorkDay(user.getId(), early, endDay);

        return new ResponseEntity<>(calendars.stream().map(calendar -> {
            int pay = Pay(calendar.getWorkingTime(), calendar.getHourlyWage());
            return new CalendarResponseDto(calendar, pay);
        }).collect(Collectors.toList()), HttpStatus.OK);
    }
    //근무달력조회(일별)
    public ResponseEntity<?> dayCalendar(String day) {
        User user = SecurityUtil.getCurrentUser();
        log.info(day);
        LocalDate early = LocalDate.parse(day, DateTimeFormatter.ofPattern("yyyyMMdd"));
        List<Calendar> calendars = calendarRepository.findAllByUserIdAndWorkDay(user.getId(), early);
        return new ResponseEntity<>(calendars.stream().map(calendar -> {
            int pay = Pay(calendar.getWorkingTime(), calendar.getHourlyWage());
            return new CalendarResponseDto(calendar, pay);
        }).collect(Collectors.toList()), HttpStatus.OK);
    }


    //시급계산
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

    //주휴수당겟요청
    public ResponseEntity<?> bonus(String month) {
        System.out.println(month);
        month += "01";
        LocalDate early = LocalDate.parse(month, DateTimeFormatter.ofPattern("yyyyMMdd"));
        System.out.println("==============" + early);
        User user = SecurityUtil.getCurrentUser();
        //직장정보
        List<WorkPlace> workPlaces = workPlaceRepository.findAllByUserId(user.getId());
        List<CalendarResponseDto.BonusResponseDto> BonusResponseDto = new ArrayList<>();
        workPlaces.forEach(workPlace -> {
            LocalDate today = early;
            List<CalendarResponseDto.StatutoryLeisurePayResponseDto> statutoryLeisurePayResponseDto = new ArrayList<>();
            AtomicInteger i = new AtomicInteger();
            while (today.isBefore(early.plusMonths(1))) {
                var startWeekDay = today.with(DayOfWeek.MONDAY);
                var dates = startWeekDay.datesUntil(startWeekDay.plusWeeks(1)).collect(Collectors.toList());
                AtomicReference<LocalTime> totalTime = new AtomicReference<>(LocalTime.parse(LocalTime.parse("00:00").format(DateTimeFormatter.ofPattern("HH:mm"))));
                dates.stream().map(date -> {
                    Calendar calendar = calendarRepository.findByWorkDayAndAndWorkPlaceId(date, workPlace.getId()).orElse(new Calendar());
                    if (calendar.getWorkingTime() != null) {
                        totalTime.set(totalTime.get().plusHours(calendar.getWorkingTime().getHour()).plusMinutes(calendar.getWorkingTime().getMinute()));
                    }
                    return totalTime;
                }).collect(Collectors.toList());

                LocalTime total = LocalTime.parse(totalTime.toString());
                boolean result = total.equals(LocalTime.parse(LocalTime.parse("15:00").format(DateTimeFormatter.ofPattern("HH:mm")))) || total.isAfter(LocalTime.parse(LocalTime.parse("15:00").format(DateTimeFormatter.ofPattern("HH:mm"))));
                var sun = today.with(DayOfWeek.SUNDAY);
                statutoryLeisurePayResponseDto.add(new CalendarResponseDto.StatutoryLeisurePayResponseDto(result, total, sun));
                today = today.plusDays(7);
            }

            statutoryLeisurePayResponseDto.forEach(list -> {
                if (list.isResult()) {
                    AtomicInteger Origin = new AtomicInteger();
                    //int i = 0;
                    List<Calendar> calendarList = calendarRepository.findAllByUserIdAndAndWorkPlaceId(user.getId(), workPlace.getId());
                    calendarList.forEach(calendar -> {
                        Origin.addAndGet(calendar.getHourlyWage());
                    });
                    double payOrigin = Integer.parseInt(String.valueOf(Origin)) / calendarList.size();
                    log.info("----------------------" + payOrigin);
                    log.info("true일때 주휴수당 o ---------------------------------------------------");
                    i.set(StatutoryLeisurePay(list.getTotalTime(), payOrigin));
                    log.info("총주휴수당--------------------------------------------" + i);
                    int bonus = Integer.parseInt(i.toString());
                    BonusResponseDto.add(new CalendarResponseDto.BonusResponseDto(bonus, list.getSunday(), workPlace));

                }
            });
//            var sunday = today.with(DayOfWeek.SUNDAY);
        });
        return new ResponseEntity<>(BonusResponseDto, HttpStatus.OK);
    }


    //주휴수당계산
    @Transactional
    public int StatutoryLeisurePay(LocalTime totalTime, double payOrigin) {
        int hour = Integer.parseInt(totalTime.format(DateTimeFormatter.ofPattern("HH")));
        double minute = Integer.parseInt(totalTime.format(DateTimeFormatter.ofPattern("mm"))) / 60;
        double total = hour + minute;
        log.info("토탈시간----" + total);


        return (int) (total * payOrigin) / 5;
    }

    public ResponseEntity<?> getTotalPay(String month) {
        log.info(month);
        User user = SecurityUtil.getCurrentUser();
        month += "01";
        LocalDate early = LocalDate.parse(month, DateTimeFormatter.ofPattern("yyyyMMdd"));
        LocalDate endDay = early.withDayOfMonth(early.lengthOfMonth());
        log.info("월초 - " + early + "----" + "월말 - " + endDay);
        List<Calendar> calendars = calendarRepository.findByUserIdAndWorkDay(user.getId(), early, endDay);
        AtomicInteger totalPay = new AtomicInteger();
        calendars.forEach(calendar -> {
            totalPay.addAndGet(Pay(calendar.getWorkingTime(), calendar.getHourlyWage()));
        });
        int total = Integer.parseInt(String.valueOf(totalPay));
        return new ResponseEntity<>(new CalendarResponseDto.totalPayResponseDto(total, early), HttpStatus.OK);
    }

    public ResponseEntity<?> deleteDay(Long todoId) {
        Calendar calendar = calendarRepository.findById(todoId).orElse(new Calendar());
        User user = SecurityUtil.getCurrentUser();
        if(!calendar.getUser().getId().equals(user.getId())){
            throw new RestApiException(CommonStatusCode.INVALID_USER_DELETE);
        }
        calendarRepository.deleteById(todoId);
        return new ResponseEntity<>(new MsgResponseDto("근무지를 삭제하였습니다."), HttpStatus.OK);
    }


}
