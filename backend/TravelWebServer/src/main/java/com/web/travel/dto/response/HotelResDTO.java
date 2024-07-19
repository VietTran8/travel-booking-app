package com.web.travel.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HotelResDTO {
    private Long id;
    private String name;
    private String address;
    private String illustration;
    private List<RoomResDTO> rooms;
}
