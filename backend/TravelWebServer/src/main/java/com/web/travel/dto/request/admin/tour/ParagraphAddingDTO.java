package com.web.travel.dto.request.admin.tour;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParagraphAddingDTO {
    private int order;
    private String content;
    private String imageName;
    private String oldImage;
    private int layout;
}
