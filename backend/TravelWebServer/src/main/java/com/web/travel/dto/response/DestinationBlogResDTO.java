package com.web.travel.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DestinationBlogResDTO {
    private Long id;
    private String title;
    private String type;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private Date time;
    private long views;
    private String author;
    private String img;
    private String description;
    @JsonFormat(pattern = "HH:mm:ss dd/MM/yyyy")
    private Date activityTime;
    private Long activityId;
}
