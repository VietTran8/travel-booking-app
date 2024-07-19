package com.web.travel.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class ResDTO {
    private int code;
    private boolean status;
    private String message;
    private Object data;
}
