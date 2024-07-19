package com.web.travel.mapper.response;

import com.web.travel.dto.response.DestinationBlogResDTO;
import com.web.travel.mapper.Mapper;
import com.web.travel.model.DestinationBlog;

public class DestinationBlogResMapper implements Mapper {
    @Override
    public Object mapToDTO(Object obj) {
        DestinationBlogResDTO dto = new DestinationBlogResDTO();
        if(obj instanceof DestinationBlog){
            obj = (DestinationBlog) obj;
            dto.setId(((DestinationBlog) obj).getId());
            dto.setTime(((DestinationBlog) obj).getPostDate());
            if (((DestinationBlog) obj).getAuthor() != null && !((DestinationBlog) obj).getAuthor().isEmpty())
                dto.setAuthor(((DestinationBlog) obj).getAuthor());
            else
                dto.setAuthor(((DestinationBlog) obj).getUser().getFullName());
            dto.setType(((DestinationBlog) obj).getType());
            dto.setTitle(((DestinationBlog) obj).getTitle());
            dto.setViews(((DestinationBlog) obj).getViews());
            dto.setImg(((DestinationBlog) obj).getBlog().getBackgroundImg());
        }
        return dto;
    }

    @Override
    public Object mapToObject(Object obj) {
        return null;
    }
}
