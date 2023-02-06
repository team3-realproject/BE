package com.example.alba_pocket.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TotalCountMessageDto {
    private int totalCount;

    public TotalCountMessageDto(Integer count) {
        this.totalCount = count;
    }
}
