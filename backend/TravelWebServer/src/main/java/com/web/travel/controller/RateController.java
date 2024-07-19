package com.web.travel.controller;

import com.web.travel.dto.ResDTO;
import com.web.travel.dto.request.common.RateReqDTO;
import com.web.travel.dto.request.common.RateUpdateReqDTO;
import com.web.travel.dto.response.RateResDTO;
import com.web.travel.service.RateService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/rate")
public class RateController {
    @Autowired
    RateService rateService;
    @GetMapping("/{tourId}")
    public ResponseEntity<?> getRateByTour(
            Principal principal,
            @PathVariable("tourId") long tourId,
            @RequestParam(defaultValue = "1", required = false) int page,
            @RequestParam(defaultValue = "100000", required = false) int limit
    ){
        Map<String, Object> response = rateService.getRates(principal, tourId, page, limit, true);
        return ResponseEntity.ok(
            new ResDTO(
                HttpServletResponse.SC_OK,
                response != null,
                response != null ? "Rates fetched successfully" : "Rate not found with tour id: " + tourId,
                response
            )
        );
    }

    @GetMapping("/blog/{blogId}")
    public ResponseEntity<?> getRateByBlog(
            Principal principal,
            @PathVariable("blogId") long blogId,
            @RequestParam(defaultValue = "1", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int limit
    ){
        Map<String, Object> response = rateService.getRates(principal, blogId, page, limit, false);
        return ResponseEntity.ok(
                new ResDTO(
                        HttpServletResponse.SC_OK,
                        response != null,
                        response != null ? "Rates fetched successfully" : "Rate not found with blog id: " + blogId,
                        response
                )
        );
    }

    @Operation(summary = "Add rating")
    @PostMapping
    public ResponseEntity<?> addRate(Principal principal, @RequestBody RateReqDTO rating){
        return ResponseEntity.ok(
                rateService.addRate(principal, rating)
        );
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateRate(Principal principal, @RequestBody RateUpdateReqDTO rating){
        ResDTO response = rateService.updateRate(principal, rating);
        return response.isStatus() ? ResponseEntity.ok(
                response
        ) : ResponseEntity.badRequest().body(response);
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<?> deleteRate(Principal principal, @PathVariable long id){
        ResDTO response = rateService.deleteRate(principal, id);

        return response.isStatus() ? ResponseEntity.ok(
            response
        ) : ResponseEntity.badRequest().body(response);
    }
}
