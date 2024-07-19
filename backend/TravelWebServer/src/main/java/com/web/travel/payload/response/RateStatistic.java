package com.web.travel.payload.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RateStatistic {
    private long fiveStar;
    private long fourStar;
    private long threeStar;
    private long twoStar;
    private long oneStar;

    @JsonIgnore
    public long getTotalRates(){
        return fiveStar + fourStar + threeStar + twoStar + oneStar;
    }
    @JsonIgnore
    public double getAverage(){
        long totalRates = getTotalRates();
        if (totalRates != 0) {
            long sum = 5 * fiveStar + 4 * fourStar + 3 * threeStar + 2 * twoStar + oneStar;
            return sum * 1.0 / totalRates;
        } else {
            return 0.0;
        }
    }
}
