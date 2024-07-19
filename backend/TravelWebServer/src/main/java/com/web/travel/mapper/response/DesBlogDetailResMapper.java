package com.web.travel.mapper.response;

import com.web.travel.dto.response.DesBlogDetailResDTO;
import com.web.travel.dto.response.ParagraphImageResDTO;
import com.web.travel.dto.response.ParagraphResDTO;
import com.web.travel.mapper.Mapper;
import com.web.travel.model.DestinationBlog;
import com.web.travel.model.Paragraph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

@Component
public class DesBlogDetailResMapper implements Mapper {
    @Override
    public Object mapToDTO(Object obj) {
        DestinationBlog destinationBlog = (DestinationBlog) obj;
        DesBlogDetailResDTO dto = new DesBlogDetailResDTO();
        dto.setId(destinationBlog.getId());
        dto.setType(destinationBlog.getType());
        dto.setImg(destinationBlog.getBlog().getBackgroundImg());
        dto.setTitle(destinationBlog.getTitle());
        if(destinationBlog.getAuthor() == null || destinationBlog.getAuthor().isEmpty())
            dto.setAuthor(destinationBlog.getUser().getFullName());
        else
            dto.setAuthor(destinationBlog.getAuthor());
        dto.setTime(destinationBlog.getPostDate());
        
        List<ParagraphResDTO> paragraphs = destinationBlog
                .getBlog()
                .getParagraphs()
                .stream().sorted(
                    new Comparator<Paragraph>() {
                        @Override
                        public int compare(Paragraph o1, Paragraph o2) {
                            if(o1.getOrder() != null && o2.getOrder() != null)
                                return o1.getOrder() - o2.getOrder();
                            return (int) (o1.getId() - o2.getId());
                        }
                    }
                )
                .map(paragraph -> {
                    ParagraphResDTO paragraphResDTO = new ParagraphResDTO();
                    ParagraphImageResDTO img = new ParagraphImageResDTO();
                    img.setSrc(paragraph.getImgSrc());
                    img.setName(paragraph.getImgName());

                    paragraphResDTO.setImage(img);

                    paragraphResDTO.setContent(paragraph.getContent());
                    paragraphResDTO.setId(paragraph.getId());
                    paragraphResDTO.setLayout(paragraph.getLayout());
                    
                    return paragraphResDTO;
                }).toList();
        dto.setParagraphs(paragraphs);

        return dto;
    }

    @Override
    public Object mapToObject(Object obj) {
        return null;
    }
}
