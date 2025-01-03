package com.web.travel.controller;

import com.web.travel.dto.ResDTO;
import com.web.travel.payload.request.MailRequest;
import com.web.travel.service.impl.EmailServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mail")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class MailSendingController {
    private final EmailServiceImpl service;

    @PostMapping("/send")
    @CrossOrigin(origins = "*")
    public ResponseEntity<?> sendWelcomeEmail(@RequestBody MailRequest request) {
        ResDTO response = service.sendWelcomeEmail(request);
        boolean isOk = response.isStatus();
        return isOk ? ResponseEntity.ok(response) :
                ResponseEntity.badRequest().body(response);
    }
}
