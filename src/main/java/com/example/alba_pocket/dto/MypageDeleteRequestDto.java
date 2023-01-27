package com.example.alba_pocket.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
public class MypageDeleteRequestDto {

    private Data data;

    @Getter
    public static class Data{
        private Long[] commentIdList;
    }
}
