package com.example.alba_pocket.service;

import com.example.alba_pocket.dto.CalendarRequestDto;
import com.example.alba_pocket.dto.CalendarResponseDto;
import com.example.alba_pocket.dto.MsgResponseDto;
import com.example.alba_pocket.entity.Calendar;
import com.example.alba_pocket.entity.User;
import com.example.alba_pocket.entity.Work;
import com.example.alba_pocket.errorcode.CommonStatusCode;
import com.example.alba_pocket.exception.RestApiException;
import com.example.alba_pocket.repository.CalendarRepository;
import com.example.alba_pocket.repository.WorkRepository;
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
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CalendarService {


    private final WorkRepository workRepository;

    private final CalendarRepository calendarRepository;

    //근무등록
    @Transactional
    public ResponseEntity<?> createCalendar(Long placeId, CalendarRequestDto requestDto) {
        User user = SecurityUtil.getCurrentUser();
        Work work = workRepository.findById(placeId).orElseThrow(
                () -> new IllegalArgumentException("알바정보없음")
        );
        if (!user.getId().equals(work.getUser().getId())) {
            throw new IllegalArgumentException("본인아님");
        }
        List<String> workDays = Arrays.asList(requestDto.getWorkDay());
        workDays.forEach(workDay -> {
            LocalDate workDate = LocalDate.parse(workDay, DateTimeFormatter.ofPattern("yyyyMMdd"));
            LocalTime workingTime = requestDto.getEndTime().minusHours(requestDto.getStartTime().getHour()).minusMinutes(requestDto.getStartTime().getMinute());
            Calendar calendar = new Calendar(work, requestDto, user, workingTime, workDate);
            calendarRepository.saveAndFlush(calendar);
        });
        return new ResponseEntity<>(new MsgResponseDto("근무등록완료"), HttpStatus.OK);
    }

    //근무달력조회(월별)
    @Transactional(readOnly = true)
    public ResponseEntity<?> getCalendar(String month) {
        User user = SecurityUtil.getCurrentUser();
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
        LocalDate early = LocalDate.parse(day, DateTimeFormatter.ofPattern("yyyyMMdd"));

        List<Calendar> calendars = calendarRepository.findAllByUserIdAndWorkDay(user.getId(), early);
        AtomicInteger totalPay = new AtomicInteger();
        calendars.forEach(calendar -> {
            totalPay.addAndGet(Pay(calendar.getWorkingTime(), calendar.getHourlyWage()));
        });
        //일요일일때
        if(early.equals(early.with(DayOfWeek.SUNDAY))){
            totalPay.addAndGet(dailyBonus(early, user));
        }

        return new ResponseEntity<>(calendars.stream().map(calendar -> {
            int pay = Pay(calendar.getWorkingTime(), calendar.getHourlyWage());
            return new CalendarResponseDto.dailyCalendar(calendar, pay, totalPay);
        }).collect(Collectors.toList()), HttpStatus.OK);
    }

    //일별조회가 일요일일때 주휴수당가격추가
    public int dailyBonus(LocalDate day, User user){
        List<Work> works = workRepository.findAllByUserId(user.getId());
        AtomicInteger bonus = new AtomicInteger();
        works.forEach(work -> {
            var startWeekDay = day.with(DayOfWeek.MONDAY);
            var dates = startWeekDay.datesUntil(startWeekDay.plusWeeks(1)).collect(Collectors.toList());
            AtomicInteger atomicHour = new AtomicInteger();
            AtomicInteger atomicMinute = new AtomicInteger();
            dates.forEach(date ->{
                List<Calendar> calendars = calendarRepository.findAllByWorkDayAndAndWorkId(date, work.getId());
                calendars.forEach(calendar -> {
                    if (calendar.getWorkingTime() != null) {
                        atomicHour.set(Integer.parseInt(String.valueOf(atomicHour)) + calendar.getWorkingTime().getHour());
                        atomicMinute.set(Integer.parseInt(String.valueOf(atomicMinute)) + calendar.getWorkingTime().getMinute());
                    }
                });
            });
            int hour = Integer.parseInt(String.valueOf(atomicHour));
            int minute = Integer.parseInt(String.valueOf(atomicMinute));
            int plusHour = minute / 60;
            minute = minute % 60;
            hour += plusHour;
            boolean result = hour >= 15;
            if (result){
                AtomicInteger Origin = new AtomicInteger();
                List<Calendar> calendarList = calendarRepository.findAllByUserIdAndAndWorkId(user.getId(), work.getId());
                double size = calendarList.size();
                calendarList.forEach(calendar -> {
                    Origin.addAndGet(calendar.getHourlyWage());
                });
                double payOrigin = Integer.parseInt(String.valueOf(Origin)) / size;
                log.info("----------------------" + payOrigin);
                bonus.set(StatutoryLeisurePay(hour, minute, payOrigin));
                log.info("총주휴수당--------------------------------------------" + bonus);
            }
        });
        int intBonus = Integer.parseInt(String.valueOf(bonus));
        return intBonus;
    }
    //근무달력조회(일별)주휴수당요청(일요일일때만실행)
    public ResponseEntity<?> dayBonus(String day) {
        User user = SecurityUtil.getCurrentUser();
        LocalDate early = LocalDate.parse(day, DateTimeFormatter.ofPattern("yyyyMMdd"));
        //일요일인지확인
        if(early.equals(early.with(DayOfWeek.SUNDAY))){
            List<Work> works = workRepository.findAllByUserId(user.getId());
            List<CalendarResponseDto.dayBonusResponseDto> BonusResponseDto = new ArrayList<>();
            works.forEach(work -> {
                List<CalendarResponseDto.StatutoryLeisurePayResponseDto> statutoryLeisurePayResponseDto = new ArrayList<>();
                var startWeekDay = early.with(DayOfWeek.MONDAY);
                var dates = startWeekDay.datesUntil(startWeekDay.plusWeeks(1)).collect(Collectors.toList());
                AtomicInteger atomicHour = new AtomicInteger();
                AtomicInteger atomicMinute = new AtomicInteger();
                dates.forEach(date ->{
                    List<Calendar> calendars = calendarRepository.findAllByWorkDayAndAndWorkId(date, work.getId());
                    calendars.forEach(calendar -> {
                        if (calendar.getWorkingTime() != null) {
                            atomicHour.set(Integer.parseInt(String.valueOf(atomicHour)) + calendar.getWorkingTime().getHour());
                            atomicMinute.set(Integer.parseInt(String.valueOf(atomicMinute)) + calendar.getWorkingTime().getMinute());
                        }
                    });
                });
                int hour = Integer.parseInt(String.valueOf(atomicHour));
                int minute = Integer.parseInt(String.valueOf(atomicMinute));
                int plusHour = minute / 60;
                minute = minute % 60;
                hour += plusHour;
                boolean result = hour >= 15;
                statutoryLeisurePayResponseDto.add(new CalendarResponseDto.StatutoryLeisurePayResponseDto(result, hour, minute, early));

                statutoryLeisurePayResponseDto.forEach(list -> {
                    if (list.isResult()) {
                        AtomicInteger Origin = new AtomicInteger();
                        List<Calendar> calendarList = calendarRepository.findAllByUserIdAndAndWorkId(user.getId(), work.getId());
                        double size = calendarList.size();
                        calendarList.forEach(calendar -> {
                            Origin.addAndGet(calendar.getHourlyWage());
                        });
                        double payOrigin = Integer.parseInt(String.valueOf(Origin)) / size;
                        int bonus = StatutoryLeisurePay(list.getHour(), list.getMinute(), payOrigin);
                        LocalDate monday = list.getSunday().with(DayOfWeek.MONDAY);
                        BonusResponseDto.add(new CalendarResponseDto.dayBonusResponseDto(bonus, list.getSunday(), work, monday));
                    }
                });
            });
            return new ResponseEntity<>(BonusResponseDto, HttpStatus.OK);
        }
       return null;
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

    //근무달력조회(월별)주휴수당요청
    public ResponseEntity<?> bonus(String month) {
        System.out.println(month);
        month += "01";
        LocalDate early = LocalDate.parse(month, DateTimeFormatter.ofPattern("yyyyMMdd"));
        System.out.println("==============" + early);
        User user = SecurityUtil.getCurrentUser();
        //직장정보
        List<Work> works = workRepository.findAllByUserId(user.getId());
        List<CalendarResponseDto.BonusResponseDto> BonusResponseDto = new ArrayList<>();
        works.forEach(work -> {
            LocalDate today = early;
            List<CalendarResponseDto.StatutoryLeisurePayResponseDto> statutoryLeisurePayResponseDto = new ArrayList<>();
            while (today.isBefore(early.plusMonths(1))) {
                var startWeekDay = today.with(DayOfWeek.MONDAY);
                var dates = startWeekDay.datesUntil(startWeekDay.plusWeeks(1)).collect(Collectors.toList());
                AtomicReference<LocalTime> totalTime = new AtomicReference<>(LocalTime.parse(LocalTime.parse("00:00").format(DateTimeFormatter.ofPattern("HH:mm"))));
                AtomicInteger atomicHour = new AtomicInteger();
                AtomicInteger atomicMinute = new AtomicInteger();
                dates.stream().map(date -> {
                    List<Calendar> calendars = calendarRepository.findAllByWorkDayAndAndWorkId(date, work.getId());
                    calendars.forEach(calendar -> {
                        if (calendar.getWorkingTime() != null) {
                            atomicHour.set(Integer.parseInt(String.valueOf(atomicHour)) + calendar.getWorkingTime().getHour());
                            atomicMinute.set(Integer.parseInt(String.valueOf(atomicMinute)) + calendar.getWorkingTime().getMinute());
                        }
                    });
                    return totalTime;
                }).collect(Collectors.toList());
                int hour = Integer.parseInt(String.valueOf(atomicHour));
                int minute = Integer.parseInt(String.valueOf(atomicMinute));
                int plusHour = minute / 60;
                minute = minute % 60;
                hour += plusHour;
                boolean result = hour >= 15;
                log.info(String.valueOf(result));
                LocalTime total = LocalTime.parse(totalTime.toString());
                var sunday = today.with(DayOfWeek.SUNDAY);
                statutoryLeisurePayResponseDto.add(new CalendarResponseDto.StatutoryLeisurePayResponseDto(result, hour, minute, sunday));
                today = today.plusDays(7);
            }

            statutoryLeisurePayResponseDto.forEach(list -> {
                if (list.isResult()) {
                    AtomicInteger Origin = new AtomicInteger();
                    //int i = 0;
                    List<Calendar> calendarList = calendarRepository.findAllByUserIdAndAndWorkId(user.getId(), work.getId());
                    double size = calendarList.size();
                    calendarList.forEach(calendar -> {
                        Origin.addAndGet(calendar.getHourlyWage());
                    });
                    double payOrigin = Integer.parseInt(String.valueOf(Origin)) / size;
                    log.info("----------------------" + payOrigin);
                    int bonus = StatutoryLeisurePay(list.getHour(), list.getMinute(), payOrigin);
                    log.info("총주휴수당--------------------------------------------" + bonus);
                    BonusResponseDto.add(new CalendarResponseDto.BonusResponseDto(bonus, list.getSunday(), work));

                }
            });
        });
        return new ResponseEntity<>(BonusResponseDto, HttpStatus.OK);
    }


    //주휴수당계산
    @Transactional
    public int StatutoryLeisurePay(int hour, int minute,  double payOrigin) {
        double minute1 = minute / 60.0;
        double total = hour + minute1;
        log.info("토탈시간----" + total);
        return (int) (total * payOrigin) / 5;
    }

    //월총급여계산(주휴수당은아직계산x)
    public ResponseEntity<?> getTotalPay(String month) {
        User user = SecurityUtil.getCurrentUser();
        month += "01";
        LocalDate early = LocalDate.parse(month, DateTimeFormatter.ofPattern("yyyyMMdd"));
        LocalDate endDay = early.withDayOfMonth(early.lengthOfMonth());
        List<Calendar> calendars = calendarRepository.findByUserIdAndWorkDay(user.getId(), early, endDay);
        AtomicInteger totalPay = new AtomicInteger();
        calendars.forEach(calendar -> {
            totalPay.addAndGet(Pay(calendar.getWorkingTime(), calendar.getHourlyWage()));
        });
        int total = Integer.parseInt(String.valueOf(totalPay));
        return new ResponseEntity<>(new CalendarResponseDto.totalPayResponseDto(total, early), HttpStatus.OK);
    }

    //근무삭제
    public ResponseEntity<?> deleteDay(Long todoId) {
        Calendar calendar = calendarRepository.findById(todoId).orElse(new Calendar());
        User user = SecurityUtil.getCurrentUser();
        if (!calendar.getUser().getId().equals(user.getId())) {
            throw new RestApiException(CommonStatusCode.INVALID_USER_DELETE);
        }
        calendarRepository.deleteById(todoId);
        return new ResponseEntity<>(new MsgResponseDto("근무지를 삭제하였습니다."), HttpStatus.OK);
    }

    //수정겟요청
    public ResponseEntity<?> getUpdateCalendar(Long todoId) {
        User user = SecurityUtil.getCurrentUser();
        Calendar calendar = calendarRepository.findById(todoId).orElseThrow(
                ()-> new IllegalArgumentException("없는 근무입니다.")
        );


        return new ResponseEntity<>(new CalendarResponseDto.updateGetResponseDto(calendar), HttpStatus.OK);
    }
    //수정
    @Transactional
    public ResponseEntity<?> updateCalendar(Long todoId, CalendarRequestDto requestDto) {
        User user = SecurityUtil.getCurrentUser();
        Calendar calendar = calendarRepository.findById(todoId).orElse(new Calendar());
        if (!calendar.getUser().getId().equals(user.getId())) {
            throw new RestApiException(CommonStatusCode.INVALID_USER_UPDATE);
        }
        LocalTime workingTime = requestDto.getEndTime().minusHours(requestDto.getStartTime().getHour()).minusMinutes(requestDto.getStartTime().getMinute());
        calendar.update(requestDto, workingTime);
        return new ResponseEntity<>("수정완료", HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> workTotalPay(Long placeId) {
        User user = SecurityUtil.getCurrentUser();
        LocalDate early = LocalDate.now();
        LocalDate startDay = early.withDayOfMonth(1);
        LocalDate endDay = early.withDayOfMonth(early.lengthOfMonth());

        List<Calendar> calendars = calendarRepository.workTotalPay(user.getId(), placeId, startDay, endDay);

        System.out.println(calendars.size());
        AtomicInteger total = new AtomicInteger();
        calendars.forEach(calendar -> {
            total.addAndGet(Pay(calendar.getWorkingTime(), calendar.getHourlyWage()));
        });
        int totalWage = Integer.parseInt(String.valueOf(total));

        return new ResponseEntity<>(new CalendarResponseDto.WorkTotalPayResponseDto(placeId, startDay, totalWage), HttpStatus.OK);
    }
}
