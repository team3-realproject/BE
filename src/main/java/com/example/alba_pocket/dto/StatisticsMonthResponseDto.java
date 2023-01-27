package com.example.alba_pocket.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class StatisticsMonthResponseDto {
    private List<PlaceFiveMonthDto> series = new ArrayList<>();
    private List<String> colors = new ArrayList<>();
    private List<String> categories = new ArrayList<>();



    public void addSeries(PlaceFiveMonthDto placeFiveMonthDto) {
        series.add(placeFiveMonthDto);
    }
    public void addColor(String color) {
        colors.add(color);
    }
    public void addCategory(String category) {
        categories.add(category);
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class PlaceFiveMonthDto {
        private String placeName;
        List<Integer> data = new ArrayList<>();

        public void addData(int pay) {
            data.add(pay);
        }

        public PlaceFiveMonthDto(String placeName) {
            this.placeName = placeName;
        }
    }

}
