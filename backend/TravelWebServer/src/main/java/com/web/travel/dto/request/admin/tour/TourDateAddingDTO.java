package com.web.travel.dto.request.admin.tour;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TourDateAddingDTO {
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date departDate;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date endDate;
    private int currentPeople;
    private int maxPeople;
    private String dateType;
    private double adultPrice;
    private double childPrice;
}
