package com.web.travel.controller;

import com.web.travel.dto.ResDTO;
import com.web.travel.payload.request.NewPasswordRequest;
import com.web.travel.payload.request.MailRequest;
import com.web.travel.payload.request.MailResetPasswordRequest;
import com.web.travel.service.AuthService;
import com.web.travel.service.email.EmailService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/reset-password")
@CrossOrigin(origins = "*")
public class ResetPasswordController {
    @Autowired
    EmailService emailService;
    @Autowired
    AuthService authService;
    @Value("${travel.app.client.host}")
    String clientHost;

    @GetMapping("/check")
    public ResponseEntity<?> validateToken(@RequestParam(value = "token", required = true) String encodedToken){
        String token = authService.decodeResetPasswordToken(encodedToken);
        if(!authService.resetPasswordTokenIsValid(token)){
            return ResponseEntity.ok(
                new ResDTO(
                        HttpServletResponse.SC_BAD_REQUEST,
                        false,
                        "Token is not valid",
                        ""
                )
            );
        }
        return ResponseEntity.ok(
            new ResDTO(
                    HttpServletResponse.SC_OK,
                    true,
                    "Token is valid",
                    ""
            )
        );
    }

    @PostMapping("/change/{token}")
    public ResponseEntity<?> changePassword(@RequestBody NewPasswordRequest password, @PathVariable("token") String encodedToken){
        String token = authService.decodeResetPasswordToken(encodedToken);
        if(authService.resetPasswordTokenIsValid(token)){
            return ResponseEntity.ok(
                    authService.resetPassword(password.getNewPassword(), token)
            );
        }
        return ResponseEntity.badRequest().body(
            new ResDTO(
                    HttpServletResponse.SC_BAD_REQUEST,
                    false,
                    "Token is not valid",
                    ""
            )
        );
    }
    @PostMapping("/send-mail")
    public ResponseEntity<?> mailSending(@RequestBody MailResetPasswordRequest mail){
        String url = "http://localhost:8080/api/reset-password/redirect?token=";
        String token = authService.createResetPasswordToken(mail.getEmail());
        String userFullName = authService.getUserFullNameFromEmail(mail.getEmail());

        if(userFullName.isEmpty()){
            return ResponseEntity.badRequest().body(
                    new ResDTO(
                            HttpServletResponse.SC_BAD_REQUEST,
                            false,
                            "User not found with email: " + mail.getEmail(),
                            null
                    )
            );
        }
        String encodedToken = authService.encodeResetPasswordToken(token);
        url += encodedToken;
        MailRequest mailRequest = new MailRequest();
        mailRequest.setSubject("RESET PASSWORD");
        mailRequest.setTo(mail.getEmail());
        mailRequest.setFrom("travel-vn");
        Map<String, Object> model = new HashMap<>();
        model.put("url", url);
        model.put("name", userFullName);
        model.put("clientHost", clientHost);
        if(emailService.sendResetPasswordEmail(mailRequest, model).isStatus()){
            return ResponseEntity.ok(
                    new ResDTO(
                        HttpServletResponse.SC_OK,
                        true,
                        "Mail sent successfully",
                        null
                    )
            );
        }
        return ResponseEntity.badRequest().body(
                new ResDTO(
                        HttpServletResponse.SC_BAD_REQUEST,
                        false,
                        "Mail sent failure",
                        null
                )
        );
    }

    @GetMapping("/redirect")
    public Object resetPassword(@RequestParam("token") String encodedToken){
        String token = authService.decodeResetPasswordToken(encodedToken);
        if (authService.resetPasswordTokenIsValid(token)) {
            String url  = clientHost + "/reset-password?token=" + authService.encodeResetPasswordToken(token);

            return ResponseEntity.status(HttpStatus.FOUND)
                    .location(URI.create(url)).build();
        }
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(clientHost)).build();
    }
}
