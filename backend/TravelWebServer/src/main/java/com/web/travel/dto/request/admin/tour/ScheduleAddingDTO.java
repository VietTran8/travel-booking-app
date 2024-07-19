package com.web.travel.dto.request.admin.tour;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ScheduleAddingDTO {
    private int order;
    private String time;
    private String content;
}
