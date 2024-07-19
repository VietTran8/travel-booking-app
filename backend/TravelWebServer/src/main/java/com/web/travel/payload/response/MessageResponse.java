package com.web.travel.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MessageResponse {
    //0: Wrong information
    //1: Ok
    private boolean status;
    private String message;
    private Object data;
}
