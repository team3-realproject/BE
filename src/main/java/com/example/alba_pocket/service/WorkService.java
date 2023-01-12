package com.example.alba_pocket.service;

import com.example.alba_pocket.dto.*;
import com.example.alba_pocket.entity.User;
import com.example.alba_pocket.entity.Work;
import com.example.alba_pocket.errorcode.CommonStatusCode;
import com.example.alba_pocket.exception.RestApiException;
import com.example.alba_pocket.repository.WorkRepository;
import com.example.alba_pocket.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
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
        WorkListResponseDto workListResponseDto = new WorkListResponseDto();
        List<Work> myWorkplace = workRepository.findByUser(user);
        for (Work work : myWorkplace) {
            WorkResponseDto workResponseDto = new WorkResponseDto(work);
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

