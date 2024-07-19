package com.web.travel.controller.admin;

import com.web.travel.dto.ResDTO;
import com.web.travel.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/admin/statistic")
public class StatisticsController {
    @Autowired
    StatisticsService statisticsService;
    @GetMapping
    @CrossOrigin(origins = "*")
    public ResponseEntity<?> getStatistics() {
        return ResponseEntity.ok(
                statisticsService.getStatistics()
        );
    }
}
