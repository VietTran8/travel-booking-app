package com.web.travel.dto.request.common;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.web.travel.model.ContactInfo;
import com.web.travel.utils.DateHandler;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderReqDTO {
    private double amount;
    private String sessionToken;
    private int adults;
    private int children;
    private String paymentMethod;
    @JsonIgnore
    private double totalPrice;
    private long tourDateId;
    private String roomType;
    private Long hotelId;
    private String specialRequest;
    private ContactInfo contactInfo;
}
