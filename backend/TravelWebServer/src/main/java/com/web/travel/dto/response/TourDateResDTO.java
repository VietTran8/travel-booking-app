package com.web.travel.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TourDateResDTO {
    private long id;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private Date departDate;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private Date endDate;
    private String type;
    private int duration;
    private double adultPrice;
    private double childPrice;
    private int maxPeople;
    private int currentPeople;
}
