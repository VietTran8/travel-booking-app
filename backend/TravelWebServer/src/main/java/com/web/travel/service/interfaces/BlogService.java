package com.web.travel.service.interfaces;

import com.web.travel.dto.ResDTO;
import com.web.travel.dto.request.admin.blog.BlogAddingReqDTO;
import com.web.travel.dto.response.DesBlogDetailResDTO;
import com.web.travel.dto.response.DestinationBlogResDTO;
import com.web.travel.model.DestinationBlog;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;
import java.util.Map;

public interface BlogService {

    Map<String, Object> getAllDestinationBlog(int page, int limit, boolean isAdmin);

    long getDesBlogCount();

    List<DestinationBlogResDTO> getTopLatestPosts(int top);

    ResDTO getBlogsByKeyword(String keyword, int page, int limit);

    List<Map<String, Object>> getListAuthorDesc();

    DestinationBlog findBlogById(long id);

    ResDTO getResById(long id);

    DesBlogDetailResDTO getBlogDetailDTOById(Long id);

    ResDTO addBlog(Principal principal, BlogAddingReqDTO blogDto, MultipartFile[] images);

    ResDTO updateBlog(Long id, Principal principal, BlogAddingReqDTO blogDto, MultipartFile[] images);

    ResDTO deleteBlog(Long id);

    ResDTO addBlogView(long id);

    ResDTO getTopBlog();
}
