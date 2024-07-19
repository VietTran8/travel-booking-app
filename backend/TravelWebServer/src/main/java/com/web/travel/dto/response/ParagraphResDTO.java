package com.web.travel.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ParagraphResDTO {
    private Long id;
    private String content;
    private ParagraphImageResDTO image;
    private int layout;
}
