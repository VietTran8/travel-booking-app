package com.web.travel.dto.request.admin.statistic;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeekStatisticDTO {
    private List<Long> lastWeek;
    private List<Long> thisWeek;
}
