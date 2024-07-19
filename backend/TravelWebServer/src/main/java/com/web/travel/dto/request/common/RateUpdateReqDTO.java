package com.web.travel.dto.request.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RateUpdateReqDTO {
    private long id;
    private String comment;
    private int star;
}
