package com.web.travel.controller;

import com.web.travel.dto.ResDTO;
import com.web.travel.model.Order;
import com.web.travel.repository.OrderRepository;
import com.web.travel.service.AuthService;
import com.web.travel.service.interfaces.FileUploadService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/test")
public class TestController {
    @Autowired
    private AuthService authService;
    @Autowired
    private FileUploadService fileUploadService;
    @Autowired
    OrderRepository orderRepository;
    @GetMapping("/all")
    public String allAccess() {
        return "Public Content.";
    }

    @GetMapping("/user")
    public String userAccess() {
        return "User Content.";
    }

    @GetMapping("/tour")
    public String tourAccess() {
        return "Tour Management Board.";
    }

    @GetMapping("/admin")
//    @PreAuthorize("hasRole('ADMIN')")
    public String adminAccess(@RequestHeader("Authorization") String token){
        String email = authService.getEmailFromToken(token);
        return "Admin Board." + email;
    }

    @PostMapping("/add/order")
    public String addOrder(@Valid @RequestBody Order order){
        orderRepository.save(order);
        return "oK";
    }

    @GetMapping("/get/order")
    public List<Order> getOrder(){
        return orderRepository.findAll();
    }
    @PostMapping("/upload")
    public ResponseEntity<ResDTO> uploadFiles(@RequestParam("files") MultipartFile[] files) throws IOException {
        List<String> fileNames = fileUploadService.uploadMultiFile(files);
        return ResponseEntity.ok(
            new ResDTO(HttpServletResponse.SC_OK,
                    true,
                    "Files uploaded successfully",
                    fileNames)
        );
    }
}