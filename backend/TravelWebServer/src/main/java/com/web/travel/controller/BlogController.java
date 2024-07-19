package com.web.travel.controller;

import com.web.travel.dto.ResDTO;
import com.web.travel.service.BlogService;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/blog")
@CrossOrigin(origins = "*")
public class BlogController {
    @Autowired
    BlogService blogService;

    @GetMapping("/all")
    @CrossOrigin(origins = "*")
    public ResponseEntity<?> getAll(
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit
    ){
        return ResponseEntity.ok(
                new ResDTO(HttpServletResponse.SC_OK,
                        true,
                        "Blog fetched successfully",
                        blogService.getAllDestinationBlog(
                                page - 1,
                                limit,
                                false
                        )
                )
        );
    }

    @GetMapping("search")
    @CrossOrigin(origins = "*")
    public ResponseEntity<?> search(
            @RequestParam String key,
            @RequestParam(defaultValue = "1", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int limit
    ){
        return ResponseEntity.ok(
          blogService.getBlogsByKeyword(key, page, limit)
        );
    }

    @GetMapping("/latest")
    @CrossOrigin(origins = "*")
    public ResponseEntity<?> getLatest(){
        return ResponseEntity.ok(
                new ResDTO(
                        HttpServletResponse.SC_OK,
                        true,
                        "Blog fetched successfully",
                        blogService.getTopLatestPosts(4)
                )
        );
    }

    @GetMapping("/authors")
    @CrossOrigin(origins = "*")
    public ResponseEntity<?> getAuthorsDesc(){
        return ResponseEntity.ok(
            new ResDTO(
                HttpServletResponse.SC_OK,
                true,
                "Authors fetched successfully",
                blogService.getListAuthorDesc()
            )
        );
    }

    @GetMapping("/{id}")
    @CrossOrigin(origins = "*")
    public ResponseEntity<?> getBlogById(@PathVariable long id){
        ResDTO response = blogService.getResById(id);
        if(response.isStatus()){
            return ResponseEntity.ok(
                    response
            );
        }
        return ResponseEntity.badRequest().body(
                response
        );
    }

    @PostMapping("/add-view/{id}")
    public ResponseEntity<ResDTO> addBlogView(@PathVariable long id){
        ResDTO response = blogService.addBlogView(id);
        return response.isStatus() ? ResponseEntity.ok(
                response
        ) : ResponseEntity.badRequest().body(
                response
        );
    }

    @GetMapping("/top")
    public ResponseEntity<ResDTO> getTopBlogs(){
        return ResponseEntity.ok(
                blogService.getTopBlog()
        );
    }
}
