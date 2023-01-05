package com.example.alba_pocket.service;

import com.example.alba_pocket.dto.CalendarRequestDto;
import com.example.alba_pocket.dto.CalendarResponseDto;
import com.example.alba_pocket.entity.Calendar;
import com.example.alba_pocket.entity.User;
import com.example.alba_pocket.entity.WorkPlace;
import com.example.alba_pocket.repository.CalendarRepository;
import com.example.alba_pocket.repository.WorkPlaceRepository;
import com.example.alba_pocket.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CalendarService {

    private final WorkPlaceRepository workPlaceRepository;

    private final CalendarRepository calendarRepository;

    public ResponseEntity<?> createCalendar(Long workplaceId, CalendarRequestDto requestDto) {
        User user = SecurityUtil.getCurrentUser();
        WorkPlace workPlace = workPlaceRepository.findById(workplaceId).orElseThrow(
                () -> new IllegalArgumentException("알바정보없음")
        );
        int time = requestDto.getTime();
        int payOrigin = requestDto.getPayOrigin();
        int pay = Pay(time, payOrigin);
        Calendar calendar = new Calendar(workPlace, requestDto, user);
        calendarRepository.saveAndFlush(calendar);

        return new ResponseEntity<>(new CalendarResponseDto(calendar, pay), HttpStatus.OK);
    }

    public int Pay(int time, int payOrigin){
        int pay = time * payOrigin;

        return pay;
    }

}
