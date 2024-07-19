package com.web.travel.dto.request.admin.hotel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class HotelAddingDTO {
    private String name;
    private String address;
    private List<RoomAddingDTO> rooms;
}
