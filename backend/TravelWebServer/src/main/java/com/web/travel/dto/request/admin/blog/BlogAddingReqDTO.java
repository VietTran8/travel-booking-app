package com.web.travel.dto.request.admin.blog;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.web.travel.dto.request.admin.tour.ParagraphAddingDTO;
import com.web.travel.model.Paragraph;
import com.web.travel.utils.DateHandler;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlogAddingReqDTO {
    private String title;
    private String type;
    private String author;
    private final Date postDate = DateHandler.getCurrentDateTime();
    private List<ParagraphAddingDTO> paragraphs;
    @JsonIgnore
    private String userEmail;
}
