package com.web.travel.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RateResDTO {
    private long id;
    private String username;
    private String email;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private Date ratedDate;
    private int star;
    private String comment;
    private String avatar;
    private boolean isActive;
}
