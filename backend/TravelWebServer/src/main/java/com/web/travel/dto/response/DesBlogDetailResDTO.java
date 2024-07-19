package com.web.travel.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DesBlogDetailResDTO {
    private Long id;
    private String title;
    private String type;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private Date time;
    private String author;
    private String img;
    private List<ParagraphResDTO> paragraphs;
}
