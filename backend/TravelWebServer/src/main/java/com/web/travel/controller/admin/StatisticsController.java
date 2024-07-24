package com.web.travel.controller.admin;

import com.web.travel.service.impl.StatisticsServiceImpl;
import com.web.travel.service.interfaces.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/statistic")
@RequiredArgsConstructor
public class StatisticsController {
    private final StatisticsService statisticsService;

    @GetMapping
    @CrossOrigin(origins = "*")
    public ResponseEntity<?> getStatistics() {
        return ResponseEntity.ok(
                statisticsService.getStatistics()
        );
    }
}
