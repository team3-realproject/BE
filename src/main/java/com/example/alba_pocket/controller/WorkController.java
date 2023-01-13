package com.example.alba_pocket.controller;

import com.example.alba_pocket.dto.WorkRequestDto;
import com.example.alba_pocket.service.WorkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/workplace")
public class WorkController {
    private final WorkService workService;


    @PostMapping
    public ResponseEntity<?> createWorkplace(@RequestBody WorkRequestDto workRequestDto) {
        return workService.createWorkplace(workRequestDto);
    }

    @GetMapping
    public ResponseEntity<?> getMyWorkplace() {
        return workService.getMyWorkplace();
    }

    @GetMapping("/{placeId}")
    public ResponseEntity<?> getSelectWorkplace(@PathVariable Long placeId) {
        return workService.getSelectWorkplace(placeId);
    }

    @PutMapping("/{placeId}")
    public ResponseEntity<?> updateMyWorkplace(@PathVariable Long placeId, @RequestBody WorkRequestDto workRequestDto) {
        return workService.updateMyWorkplace(placeId, workRequestDto);
    }

    @DeleteMapping("/{placeId}")
    public ResponseEntity<?> deleteMyWorkplace(@PathVariable Long placeId) {
        return workService.deleteMyWorkplace(placeId);
    }
}
