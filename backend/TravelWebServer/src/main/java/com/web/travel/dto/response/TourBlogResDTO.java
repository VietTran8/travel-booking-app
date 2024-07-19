package com.web.travel.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TourBlogResDTO {
    private Long id;
    private String backgroundImage;
    private List<ParagraphResDTO> paragraphs;
}
