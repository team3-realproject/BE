package com.example.alba_pocket.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.joda.time.DateTime;
import org.springframework.format.annotation.DateTimeFormat;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class StatisticsResponseDto {
    private List<String> labels = new ArrayList<>();
    private List<String> colors = new ArrayList<>();
    private List<Integer> series = new ArrayList<>();

    public void addLabels (String label) {
        labels.add(label);
    }

    public void addColors (String color) {
        colors.add(color);
    }

    public void addSeries (int time) {
        series.add(time);
    }
}
