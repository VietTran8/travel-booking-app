package com.web.travel.dto.request.common;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DestinationBlogReqDTO {
    private String title;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private Date postDate;
    private Long userId;
    private String type;
    private MultipartFile backgroundImg;
    private Map<String, List<Map<MultipartFile, String>>> paragraphs;
}
