package com.web.travel.mapper.request;

import com.web.travel.dto.request.admin.tour.TourAddingDTO;
import com.web.travel.mapper.Mapper;
import com.web.travel.model.Paragraph;

import java.util.ArrayList;
import java.util.List;

public class TourParagraphsAddingMapper implements Mapper {
    @Override
    public Object mapToDTO(Object obj) {
        return null;
    }

    @Override
    public Object mapToObject(Object obj) {
        TourAddingDTO tourDTO = (TourAddingDTO) obj;
        List<Paragraph> paragraphs = new ArrayList<>();
        tourDTO.getParagraphs().forEach(paragraph -> {
            Paragraph para = new Paragraph();
            para.setContent(paragraph.getContent());
            para.setOrder(paragraph.getOrder());
            para.setImgSrc(paragraph.getOldImage());
            para.setImgName(paragraph.getImageName());
            paragraphs.add(para);
        });
        return paragraphs;
    }
}
