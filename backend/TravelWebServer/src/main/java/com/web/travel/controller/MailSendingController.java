package com.web.travel.controller;

import com.web.travel.dto.ResDTO;
import com.web.travel.payload.request.MailRequest;
import com.web.travel.service.email.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mail")
@CrossOrigin(origins = "*")
public class MailSendingController {
    @Autowired
    private EmailService service;
    @PostMapping("/send")
    @CrossOrigin(origins = "*")
    public ResponseEntity<?> sendWelcomeEmail(@RequestBody MailRequest request) {
        ResDTO response = service.sendWelcomeEmail(request);
        boolean isOk = response.isStatus();
        return isOk ? ResponseEntity.ok(response) :
                ResponseEntity.badRequest().body(response);
    }
}
