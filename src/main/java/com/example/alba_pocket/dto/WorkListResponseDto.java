package com.example.alba_pocket.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Getter
@NoArgsConstructor
public class WorkListResponseDto {
    private List<WorkResponseDto> workList = new ArrayList<>();


    public void addWork(WorkResponseDto workResponseDto) {workList.add(workResponseDto);}
}
