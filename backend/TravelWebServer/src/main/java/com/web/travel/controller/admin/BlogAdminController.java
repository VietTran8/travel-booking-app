package com.web.travel.controller.admin;

import com.web.travel.dto.ResDTO;
import com.web.travel.dto.request.admin.blog.BlogAddingReqDTO;
import com.web.travel.service.BlogService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

@RestController
@RequestMapping("api/admin/blog")
@CrossOrigin(origins = "*")
public class BlogAdminController {
    @Autowired
    BlogService blogService;

    @GetMapping("")
    public ResponseEntity<?> getAll(@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                                    @RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit
    ){
        return ResponseEntity.ok(
                new ResDTO(HttpServletResponse.SC_OK,
                    true,
                    "Blog fetched successfully",
                    blogService.getAllDestinationBlog(
                            page - 1,
                            limit,
                            true
                    )
                )
        );
    }
    @PostMapping("/add")
    @CrossOrigin(origins = "*")
    public ResponseEntity<?> addBlog(
            Principal principal,
            @RequestPart("blog") BlogAddingReqDTO blogAddingReqDTO,
            @RequestPart("images") MultipartFile[] images
            ){
        return ResponseEntity.ok(
            blogService.addBlog(principal, blogAddingReqDTO, images)
        );
    }
    @PostMapping("/update/{id}")
    @CrossOrigin(origins = "*")
    public ResponseEntity<?> updateBlog(@PathVariable("id") Long id,
            Principal principal,
            @RequestPart("blog") BlogAddingReqDTO blogAddingReqDTO,
            @RequestPart("images") MultipartFile[] images
    ){
        ResDTO response  = blogService.updateBlog(id, principal, blogAddingReqDTO, images);
        if(response.isStatus())
            return ResponseEntity.ok(response);
        return ResponseEntity.badRequest().body(response);
    }

    @PostMapping("/delete/{id}")
    @CrossOrigin(origins = "*")
    public ResponseEntity<?> deleteBlog(@PathVariable("id") Long id){
        ResDTO response = blogService.deleteBlog((id));
        if (response.isStatus())
            return ResponseEntity.ok(response);
        return ResponseEntity.badRequest().body(response);
    }
}
