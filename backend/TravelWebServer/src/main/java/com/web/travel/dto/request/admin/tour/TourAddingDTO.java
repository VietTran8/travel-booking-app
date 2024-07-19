package com.web.travel.dto.request.admin.tour;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TourAddingDTO {
    private String name;
    private String vehicle;
    private String tourType;
    private String depart;
    private String destination;
    private List<TourDateAddingDTO> tourDate;
    private List<ScheduleAddingDTO> schedules;
    private List<Long> hotelIds;
    private List<ParagraphAddingDTO> paragraphs;
}
