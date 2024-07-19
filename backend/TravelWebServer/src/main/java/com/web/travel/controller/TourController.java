package com.web.travel.controller;

import com.web.travel.dto.ResDTO;
import com.web.travel.payload.request.TourFilter;
import com.web.travel.service.TourService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/tour")
@CrossOrigin(origins = "*")
public class TourController {
    @Autowired
    TourService tourService;
    @GetMapping("/all")
    public Object getAll(
            @RequestParam(value = "page", required =false, defaultValue = "1") Integer page,
            @RequestParam(value = "limit", required =false, defaultValue = "10") Integer limit){

        return ResponseEntity.ok(
                new ResDTO(HttpStatus.OK.value(),
                        true,
                        "Lấy dữ liệu thành công",
                        tourService.getAllTour(page - 1, limit))
        );
    }
    @GetMapping("/all/type")
    public Object getListTourGroupByType(){
        return ResponseEntity.ok(
                new ResDTO(HttpStatus.OK.value(),
                        true,
                        "Lấy dữ liệu thành công",
                        tourService.getTourDTOListGroupByType())
        );
    }
    @GetMapping("/type")
    public ResponseEntity<?> getListTourByType(@RequestParam(name = "type") String type){
        return ResponseEntity.ok(
                new ResDTO(HttpStatus.OK.value(),
                        true,
                        "Lấy dữ liệu thành công",
                        tourService.findTourByType(type))
        );
    }

    @GetMapping("/detail")
    public ResponseEntity<?> getTourDetail(Principal principal, @RequestParam("id") Long id){
        Object response = tourService.getResponseTourById(principal, id);
        String message = "";

        message = response == null ? "Tour không tồn tại!" : "Lấy tour thành công!";

        return ResponseEntity.ok(
                new ResDTO(
                        HttpStatus.OK.value(),
                        true,
                        message,
                        response
                )
        );
    }

    @GetMapping("/search")
    public ResponseEntity<?> searching(
            @RequestParam(defaultValue = "") String key,
            @RequestParam(defaultValue = "1", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int limit
    ){
        return ResponseEntity.ok(
                tourService.getTourResByNameOrDestination(key, page, limit)
        );
    }

    @GetMapping("/search-contain-blog")
    public ResponseEntity<?> searching(@RequestParam(defaultValue = "") String key){
        return ResponseEntity.ok(
                tourService.searchContainBlog(key)
        );
    }

    @PostMapping("/filter")
    public ResponseEntity<?> filter(@RequestBody TourFilter tourFilter){
        return ResponseEntity.ok(
                tourService.getTourByFilter(tourFilter)
        );
    }

    @GetMapping("/top-destination")
    public ResponseEntity<?> getTopDestination(){
        return ResponseEntity.ok(
                tourService.getTopDestinations(6)
        );
    }

    @GetMapping("/{id}/tour-date")
    public ResponseEntity<?> getTourDate(@PathVariable("id") Long id){
        ResDTO response = tourService.getTourDate(id);

        return response.isStatus() ? ResponseEntity.ok(response) : ResponseEntity.badRequest().body(response);
    }
}
