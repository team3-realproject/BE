package com.example.alba_pocket.service;

import com.example.alba_pocket.dto.WorkPlaceRequestDto;
import com.example.alba_pocket.dto.WorkPlaceResponseDto;
import com.example.alba_pocket.entity.User;
import com.example.alba_pocket.entity.WorkPlace;
import com.example.alba_pocket.repository.UserRepository;
import com.example.alba_pocket.repository.WorkPlaceRepository;
import com.example.alba_pocket.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkPlaceService {

    private final WorkPlaceRepository workPlaceRepository;

    private final UserRepository userRepository;

    @Transactional
    public ResponseEntity<?> createWorkPlace(WorkPlaceRequestDto requestDto) {
        User user = SecurityUtil.getCurrentUser();
        WorkPlace workPlace = new WorkPlace(user, requestDto);
        workPlaceRepository.saveAndFlush(workPlace);


        return new ResponseEntity<>(new WorkPlaceResponseDto(workPlace), HttpStatus.OK);
    }

    public ResponseEntity<?> getWorkPlace() {
        User user = SecurityUtil.getCurrentUser();
        List<WorkPlace> workPlaces = workPlaceRepository.findAllByUserId(user.getId());
        return new ResponseEntity<>(workPlaces.stream().map(WorkPlaceResponseDto::new).collect(Collectors.toList()), HttpStatus.OK);
    }
}
