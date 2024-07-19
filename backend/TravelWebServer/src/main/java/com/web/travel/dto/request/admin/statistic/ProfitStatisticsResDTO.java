package com.web.travel.dto.request.admin.statistic;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfitStatisticsResDTO {
    private WeekStatisticDTO statistic;
    private long profit;
    private int orderQuantity;
    private int customerQuantity;
    private long monthAverage;
}
