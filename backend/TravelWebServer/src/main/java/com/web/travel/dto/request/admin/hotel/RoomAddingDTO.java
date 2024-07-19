package com.web.travel.dto.request.admin.hotel;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RoomAddingDTO {
    private String type;
    private double price;
}
