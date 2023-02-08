package com.example.alba_pocket.service;

import com.example.alba_pocket.dto.MsgResponseDto;
import com.example.alba_pocket.dto.WorkRequestDto;
import com.example.alba_pocket.dto.WorkResponseDto;
import com.example.alba_pocket.entity.User;
import com.example.alba_pocket.entity.Work;
import com.example.alba_pocket.errorcode.CommonStatusCode;
import com.example.alba_pocket.exception.RestApiException;
import com.example.alba_pocket.repository.WorkRepository;
import com.example.alba_pocket.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class WorkService {

    private final WorkRepository workRepository;

    @Transactional
    public ResponseEntity<?> createWorkplace(WorkRequestDto workRequestDto) {
        User user = SecurityUtil.getCurrentUser();
        Work work = new Work(workRequestDto, user);
        workRepository.save(work);
        return new ResponseEntity<>(new WorkResponseDto(work), HttpStatus.OK);

    }

    @Transactional
    public ResponseEntity<?> getMyWorkplace() {
        User user = SecurityUtil.getCurrentUser();
        WorkResponseDto.WorkListResponseDto workListResponseDto = new WorkResponseDto.WorkListResponseDto();
        List<Work> myWorkplaces = workRepository.findByUser(user);
        LocalDate date = LocalDate.now();
        int lastDate = date.withDayOfMonth(date.lengthOfMonth()).getDayOfMonth();
        log.info(String.valueOf(lastDate));
        for (Work work : myWorkplaces) {
            WorkResponseDto workResponseDto = new WorkResponseDto(work, lastDate);
            workListResponseDto.addWork(workResponseDto);
        }

        return new ResponseEntity<>(workListResponseDto, HttpStatus.OK);

    }

    @Transactional
    public ResponseEntity<?> getSelectWorkplace(Long placeId) {
        Work work = workRepository.findById(placeId).orElseThrow(
                () -> new RestApiException(CommonStatusCode.NO_WORKPLACE)
        );
        return new ResponseEntity<>(new WorkResponseDto(work), HttpStatus.OK);
    }


    @Transactional
    public ResponseEntity<?> updateMyWorkplace(Long placeId, WorkRequestDto workRequestDto) {
        User user = SecurityUtil.getCurrentUser();
        Work work = workRepository.findById(placeId).orElseThrow(
                () -> new RestApiException(CommonStatusCode.NO_WORKPLACE)
        );

        if (!user.getUserId().equals(work.getUser().getUserId())) {
            throw new RestApiException(CommonStatusCode.INVALID_USER_UPDATE);
        }

        work.updateWorkPlace(workRequestDto);
        return new ResponseEntity<>(new WorkResponseDto(work), HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> deleteMyWorkplace(Long placeId) {
        User user = SecurityUtil.getCurrentUser();
        Work work = workRepository.findById(placeId).orElseThrow(
                () -> new RestApiException(CommonStatusCode.NO_WORKPLACE)
        );

        if(!user.getUserId().equals(work.getUser().getUserId())) {
            throw new RestApiException(CommonStatusCode.INVALID_USER_DELETE);
        }

        workRepository.delete(work);

        return new ResponseEntity<>(new MsgResponseDto("근무지를 삭제하였습니다."), HttpStatus.OK);
    }


}

