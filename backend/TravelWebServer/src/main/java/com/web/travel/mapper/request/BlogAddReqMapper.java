package com.web.travel.mapper.request;

import com.web.travel.dto.request.admin.blog.BlogAddingReqDTO;
import com.web.travel.mapper.Mapper;
import com.web.travel.model.Blog;
import com.web.travel.model.DestinationBlog;
import com.web.travel.model.Paragraph;
import com.web.travel.model.User;
import com.web.travel.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BlogAddReqMapper implements Mapper {
    @Autowired
    UserService userService;
    @Override
    public Object mapToDTO(Object obj) {
        return null;
    }

    @Override
    public Object mapToObject(Object obj) {
        DestinationBlog blog = new DestinationBlog();
        BlogAddingReqDTO dto = (BlogAddingReqDTO) obj;

        blog.setTitle(dto.getTitle());
        blog.setType(dto.getType());
        blog.setPostDate(dto.getPostDate());
        User user = userService.getUserObjectByEmail(dto.getUserEmail());
        blog.setUser(user);
        blog.setAuthor(dto.getAuthor());

        Blog parentBlog = new Blog();

        List<Paragraph> paragraphs = new ArrayList<>();
        dto.getParagraphs().forEach(paragraphAddingDTO -> {
            Paragraph para = new Paragraph();
            para.setBlog(parentBlog);
            para.setLayout(paragraphAddingDTO.getLayout());
            para.setOrder(paragraphAddingDTO.getOrder());
            para.setContent(paragraphAddingDTO.getContent());
            para.setImgName(paragraphAddingDTO.getImageName());
            paragraphs.add(para);
        });

        parentBlog.setParagraphs(paragraphs);

//        {
//            "title": "",
//            "type": "",
//            "paragraphs": [
//                  {
//                      "order": "",
//                      "content": "",
//                      "imageName": ""
//                  },
//                    {
//                      "order": "",
//                      "content": "",
//                      "imageName": ""
//                  },
//                    {
//                      "order": "",
//                      "content": "",
//                      "imageName": ""
//                  }
//            ],
//            "userEmail": ""
//        }

        blog.setBlog(parentBlog);

        return blog;
    }
}
