package com.web.travel.service;

import com.web.travel.dto.ResDTO;
import com.web.travel.dto.request.admin.blog.BlogAddingReqDTO;
import com.web.travel.dto.response.DesBlogDetailResDTO;
import com.web.travel.dto.response.DestinationBlogResDTO;
import com.web.travel.mapper.request.BlogAddReqMapper;
import com.web.travel.mapper.response.DesBlogDetailResMapper;
import com.web.travel.mapper.response.DestinationBlogResMapper;
import com.web.travel.mapper.Mapper;
import com.web.travel.model.*;
import com.web.travel.model.enums.EStatus;
import com.web.travel.repository.BlogRepository;
import com.web.travel.repository.DestinationBlogRepository;
import com.web.travel.repository.ParagraphRepository;
import com.web.travel.repository.custom.CustomDestinationBlogRepository;
import com.web.travel.service.cloudinary.FilesValidation;
import com.web.travel.service.interfaces.FileUploadService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.*;

@Service
public class BlogService {
    @Autowired
    BlogRepository blogRepository;
    @Autowired
    DestinationBlogRepository desRepository;
    @Autowired
    CustomDestinationBlogRepository customDesBlogRepository;
    @Autowired
    BlogAddReqMapper mapper;
    @Autowired
    FileUploadService fileUploadService;
    @Autowired
    ParagraphRepository paragraphRepository;
    @Autowired
    DesBlogDetailResMapper desBlogDetailResMapper;
    @Autowired
    FilesValidation fileValidator;
    public Map<String, Object> getAllDestinationBlog(int page, int limit, boolean isAdmin){
        Page<DestinationBlog> list;
        if(!isAdmin)
            list = customDesBlogRepository.findAllDestinationBlogDiffLatest(PageRequest.of(page, limit));
        else
            list = desRepository.findAllByOrderByPostDateDesc(PageRequest.of(page, limit));
        Map<String, Object> result = new HashMap<>();

        int pages = list.getTotalPages();

        List<DestinationBlogResDTO> listDTO = list.stream().map(blog -> {
            Mapper mapper = new DestinationBlogResMapper();
            DestinationBlogResDTO dto = (DestinationBlogResDTO) mapper.mapToDTO(blog);
            Paragraph paragraph = blog.getBlog().getParagraphs().stream().toList().get(0);
            dto.setDescription(paragraph.getContent());
            return dto;
        }).toList();

        result.put("pages", pages);
        result.put("posts", listDTO);
        return result;
    }

    public long getDesBlogCount(){
        return desRepository.count();
    }

    public List<DestinationBlogResDTO> getTopLatestPosts(int top){
        List<DestinationBlog> top4LatestBlogs = customDesBlogRepository.findTopLatestPosts(4);
        return top4LatestBlogs.stream().map(blog -> {
            Mapper mapper = new DestinationBlogResMapper();
            DestinationBlogResDTO dto = (DestinationBlogResDTO) mapper.mapToDTO(blog);
            Paragraph paragraph = blog.getBlog().getParagraphs().stream().toList().get(0);
            dto.setDescription(paragraph.getContent());
            return dto;
        }).toList();
    }

    public ResDTO getBlogsByKeyword(String keyword, int page, int limit){
        ResDTO response = new ResDTO();
        response.setCode(HttpServletResponse.SC_OK);
        response.setStatus(true);
        response.setMessage("Blog searched successfully!");

        Page<DestinationBlog> foundBlogs = desRepository.findByTitleContaining(keyword, PageRequest.of(page - 1, limit));

        List<DestinationBlogResDTO> resDTOS = foundBlogs
                .map(blog -> {
                    Mapper mapper = new DestinationBlogResMapper();
                    DestinationBlogResDTO dto = (DestinationBlogResDTO) mapper.mapToDTO(blog);
                    Paragraph paragraph = blog.getBlog().getParagraphs().stream().findFirst().orElse(null);
                    dto.setDescription(paragraph != null ? paragraph.getContent() : "");
                    return dto;
                }).toList();

        Map<String, Object> data = new HashMap<>();
        data.put("pages", foundBlogs.getTotalPages());
        data.put("posts", resDTOS);

        response.setData(data);

        return response;
    }

    public List<Map<String, Object>> getListAuthorDesc(){
        List<User> authors = customDesBlogRepository.findTopAuthor();
        List<Map<String, Object>> result = new ArrayList<>();
        authors.forEach(author -> {
            Map<String, Object> record = new HashMap<>();
            record.put("id", author.getId());
            record.put("name", author.getFullName());
            result.add(record);
        });
        return result;
    }

    public DestinationBlog findBlogById(long id){
        return desRepository.findById(id).orElse(null);
    }

    public ResDTO getResById(long id){
        DesBlogDetailResDTO dto;
        Optional<DestinationBlog> foundBlog = desRepository.findById(id);

        if(foundBlog.isPresent()){
            DestinationBlog foundBlogObj = foundBlog.get();
            dto = (DesBlogDetailResDTO) desBlogDetailResMapper.mapToDTO(foundBlogObj);

            Map<String, Object> response = new HashMap<>();
            response.put("post", dto);

            List<DestinationBlogResDTO> relevantBlogs = customDesBlogRepository.getRelevantBlogs(foundBlogObj, 4)
                    .stream().map(blog -> {
                            Mapper mapper = new DestinationBlogResMapper();
                            DestinationBlogResDTO eachDto = (DestinationBlogResDTO) mapper.mapToDTO(blog);
                            Paragraph paragraph = blog.getBlog().getParagraphs().stream().findFirst().orElse(null);
                            eachDto.setDescription(paragraph != null ? paragraph.getContent() : "");

                            return eachDto;
                        }
                    ).toList();
            response.put("relevantPosts", relevantBlogs);

            return new ResDTO(
                    HttpServletResponse.SC_OK,
                    true,
                    "Blog fetched successfully!",
                    response
            );
        }else{
            return new ResDTO(
                    HttpServletResponse.SC_BAD_REQUEST,
                    false,
                    "Could not found blog with id: " + id,
                    null
            );
        }
    }

    public DesBlogDetailResDTO getBlogDetailDTOById(Long id){
        DesBlogDetailResDTO dto;
        Optional<DestinationBlog> foundBlog = desRepository.findById(id);

        if(foundBlog.isPresent()) {
            DestinationBlog foundBlogObj = foundBlog.get();
            dto = (DesBlogDetailResDTO) desBlogDetailResMapper.mapToDTO(foundBlogObj);

            return dto;
        }
        return null;

    }

    public ResDTO addBlog(Principal principal, BlogAddingReqDTO blogDto, MultipartFile[] images){
        blogDto.setUserEmail(principal.getName());
        DestinationBlog blog = (DestinationBlog) mapper.mapToObject(blogDto);
        String backgroundImg = null;

        try{
            List<Paragraph> addedParagraph = blog
                    .getBlog()
                    .getParagraphs()
                    .stream().toList();

            List<String> fileNames = new ArrayList<>();
            if(fileValidator.validate(images) != EStatus.STATUS_EMPTY_FILE){
                fileNames = fileUploadService.uploadMultiFile(images);

                backgroundImg = fileNames.get(0);

                for (int i = 0; i < fileNames.size() - 1 && i < addedParagraph.size(); i++) {
                        addedParagraph.get(i).setImgSrc(fileNames.get(i + 1));
                        addedParagraph.get(i).setImgName(blogDto.getParagraphs().get(i).getImageName());
                }
            }

        }catch (IOException e) {
            throw new RuntimeException(e);
        }
        blog.getBlog().setBackgroundImg(backgroundImg);

        Long id = desRepository.save(blog).getId();

        DesBlogDetailResDTO response = getBlogDetailDTOById(id);

        return new ResDTO(
            HttpServletResponse.SC_OK,
            true,
            "Added blog successfully",
            response
        );
    }

    public ResDTO updateBlog(Long id, Principal principal, BlogAddingReqDTO blogDto, MultipartFile[] images){
        blogDto.setUserEmail(principal.getName());
        DestinationBlog blog = (DestinationBlog) mapper.mapToObject(blogDto);
        DestinationBlog oldBlog = desRepository.findById(id).orElse(null);

//        MultipartFile thumbnail = images[0];

        if(oldBlog!=null){

            Collection<Paragraph> paragraphs = oldBlog.getBlog().getParagraphs();

            Blog myBlog = oldBlog.getBlog();
            paragraphs.clear();
            oldBlog.setTitle(blog.getTitle());
            oldBlog.setType(blog.getType());
            oldBlog.setAuthor(blog.getAuthor());
            oldBlog.setBlog(blog.getBlog());
            oldBlog.getBlog().setBackgroundImg(myBlog.getBackgroundImg());
            blogRepository.delete(myBlog);

            String backgroundImg = null;
            try{
                List<Paragraph> updateParagraph = blog
                        .getBlog()
                        .getParagraphs()
                        .stream().toList();

                List<String> fileNames = new ArrayList<>();



                if(fileValidator.validate(images) != EStatus.STATUS_EMPTY_FILE){
                    fileNames = fileUploadService.uploadMultiFile(images);

                    backgroundImg = fileNames.get(0);
                }

                for (int i = 0; i < updateParagraph.size(); i++) {
                   if(i < fileNames.size() - 1 && fileNames.get(i + 1) != null){
                       updateParagraph.get(i).setImgSrc(fileNames.get(i + 1));
                   }else{
                       updateParagraph.get(i).setImgSrc(blogDto.getParagraphs().get(i).getOldImage());
                   }
                }

                updateParagraph.forEach(paragraph ->{
                    paragraph.setBlog(blog.getBlog());
                    paragraphRepository.save(paragraph);
                });

            }catch (IOException e) {
                throw new RuntimeException(e);
            }

            if(backgroundImg != null)
                oldBlog.getBlog().setBackgroundImg(backgroundImg);

            desRepository.save(oldBlog);

            return new ResDTO(
                    HttpServletResponse.SC_OK,
                    true,
                    "Update blog successfully",
                    oldBlog.getId()
            );
        }
        return new ResDTO(
                HttpServletResponse.SC_BAD_REQUEST,
                false,
                "Blog not found with id: " + id,
                null
        );
    }

    public ResDTO deleteBlog(Long id) {
        DestinationBlog delBlog = desRepository.findById(id).orElse(null);
        if (delBlog != null) {
            desRepository.delete(delBlog);
            return new ResDTO(
                    HttpServletResponse.SC_OK,
                    true,
                    "Delete blog successfully",
                    null
            );
        }
        return new ResDTO(
                HttpServletResponse.SC_BAD_REQUEST,
                false,
                "Blog not found with id: " + id,
                null
        );
    }

    public ResDTO addBlogView(long id){
        DestinationBlog foundBlog = findBlogById(id);
        if(foundBlog != null){
            foundBlog.setViews(foundBlog.getViews() + 1);
            desRepository.save(foundBlog);
            return new ResDTO(
                    HttpServletResponse.SC_OK,
                    true,
                    "View added successfully!",
                    foundBlog.getViews()
            );
        }
        return new ResDTO(
                HttpServletResponse.SC_BAD_REQUEST,
                false,
                "Can not found blog with id:" + id,
                null
        );
    }

    public ResDTO getTopBlog(){
        List<DestinationBlog> foundBlogs = desRepository.findTop6ByOrderByViewsDesc();
        List<DestinationBlogResDTO> resDTOS = foundBlogs
                .stream()
                .map(blog -> {
                    Mapper mapper = new DestinationBlogResMapper();
                    DestinationBlogResDTO dto = (DestinationBlogResDTO) mapper.mapToDTO(blog);
                    Paragraph paragraph = blog.getBlog().getParagraphs().stream().findFirst().orElse(null);
                    dto.setDescription(paragraph != null ? paragraph.getContent() : "");
                    return dto;
                }).toList();

        return new ResDTO(
                HttpServletResponse.SC_OK,
                true,
                "Top blogs fetched successfully",
                resDTOS
        );
    }
}
