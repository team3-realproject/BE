package com.example.alba_pocket.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Getter
@NoArgsConstructor
public class StatutoryLeisurePayResponseDto {
    private boolean result;
    private LocalTime localTime;

    public StatutoryLeisurePayResponseDto(boolean result, LocalTime local) {
        this.result = result;
        this.localTime = local;
    }
}
