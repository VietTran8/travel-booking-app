package com.web.travel.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TopDestinationResponse {
    private String id;
    private String name;
    private Long orderQuantity;
    private String img;
    private double ratedStar;
}
