package com.web.travel.payload.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TourFilter {
    private int page = 1;
    private int limit = 10;
    private Integer star;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private Date departDate;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private Date endDate;
    private Double fromPrice;
    private Double toPrice;
    private String type;
}
