package com.web.travel.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListTourResDTO {
    private String type;
    private List<TourGeneralResDTO> tours;
}
