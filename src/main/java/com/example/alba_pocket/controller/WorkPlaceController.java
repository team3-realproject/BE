package com.example.alba_pocket.controller;

import com.example.alba_pocket.dto.WorkPlaceRequestDto;
import com.example.alba_pocket.service.WorkPlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/workplace")
@RequiredArgsConstructor
public class WorkPlaceController {

    private final WorkPlaceService workPlaceService;

    @PostMapping()
    public ResponseEntity<?> createWorkPlace(@RequestBody WorkPlaceRequestDto requestDto){
        return workPlaceService.createWorkPlace(requestDto);
    }

    @GetMapping
    public ResponseEntity<?> getWorkPlace(){
        return workPlaceService.getWorkPlace();
    }

//    @PutMapping("/placeId")
//    public ResponseEntity<?> updateWorkPlace(@RequestBody WorkPlaceRequestDto requestDto, @PathVariable Long placeId){
//        return workPlaceService.updateWorkPlace(requestDto, placeId);
//    }

}
