package com.web.travel.controller.admin;

import com.web.travel.dto.ResDTO;
import com.web.travel.dto.request.admin.tour.TourAddingDTO;
import com.web.travel.service.TourService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

@RestController
@RequestMapping("/api/admin/tour")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class TourAdminController {
    private final TourService tourService;
    @Operation(summary = "Add new tour")
    @CrossOrigin(origins = "*")
    @PostMapping("/add")
    public ResponseEntity<?> addTour(
            @RequestPart("tour") TourAddingDTO tour,
            @RequestPart("images") MultipartFile[] images
    ){
        return ResponseEntity.ok(
                tourService.add(tour, images)
        );
    }

    @PostMapping("/update/{id}")
    @CrossOrigin(origins = "*")
    public ResponseEntity<?> updateTour(
            @PathVariable long id,
            @RequestPart("tour") TourAddingDTO tour,
            @RequestPart("images") MultipartFile[] images
    ){
        return ResponseEntity.ok(
                tourService.updateTour(id, tour, images)
        );
    }

    @PostMapping("delete/{id}")
    @CrossOrigin(origins = "*")
    public ResponseEntity<?> deleteTour(@PathVariable long id){
        return ResponseEntity.ok(
                tourService.deleteTour(id)
        );
    }

    @GetMapping("/all")
    @CrossOrigin(origins = "*")
    public ResponseEntity<?> getAllTour(Principal principal, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int limit){
        return ResponseEntity.ok(
                new ResDTO(HttpServletResponse.SC_OK,
                        true,
                        "Tour fetched successfully",
                        tourService.adminGetAllTour(principal, page, limit))
        );
    }
}
