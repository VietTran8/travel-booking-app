package com.web.travel.dto.request.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RateReqDTO {
    private long tourId;
    private long blogId;
    private String comment;
    private int star;
}