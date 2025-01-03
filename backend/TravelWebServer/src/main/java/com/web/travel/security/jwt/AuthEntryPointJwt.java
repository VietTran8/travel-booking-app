package com.web.travel.security.jwt;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import com.web.travel.dto.ResDTO;
import com.web.travel.service.impl.EmailServiceImpl;
import com.web.travel.service.interfaces.EmailService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
@RequiredArgsConstructor
public class AuthEntryPointJwt implements AuthenticationEntryPoint {
    private static final Logger logger = LoggerFactory.getLogger(AuthEntryPointJwt.class);
    private final EmailService emailService;
    private final JwtUtils jwtUtils;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {
        logger.error("Unauthorized error: {}", authException.getMessage());

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        String error = "Unauthorized";
        int code = HttpServletResponse.SC_UNAUTHORIZED;

        final Map<String, Object> body = new HashMap<>();
        body.put("error", error);
        body.put("path", request.getServletPath());

        String message = "";
        if(authException instanceof BadCredentialsException){
            message = "Sai email hoặc mật khẩu";
        }else if (authException instanceof InsufficientAuthenticationException){
            message = "Vui lòng đăng nhập!";
        }else if (authException instanceof AccountStatusException){
            response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);

            code = HttpServletResponse.SC_NOT_ACCEPTABLE;
            message = "Tài khoản của bạn đã bị vô hiệu hóa";

            String fullName = request.getAttribute("fullName").toString();
            String email = request.getAttribute("email").toString();

            String confirmationCode = generateConfirmationCode();
            String token = encodeResetPasswordToken(createConfirmationCodeToken(email, confirmationCode));

            emailService.sendConfirmationEmail(email, fullName, token, confirmationCode);
            body.put("activateToken", token);
        }

        ResDTO authResponse = new ResDTO(
                code,
                false,
                message,
                body
        );

        final ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), authResponse);
    }

    private String generateConfirmationCode(){
        SecureRandom rd = new SecureRandom();
        int min = 100000,
                max = 999999;
        int randomNumber = rd.nextInt(max - min + 1) + min;
        return String.valueOf(randomNumber);
    }

    private String encodeResetPasswordToken(String token){
        Base64.Encoder encoder = Base64.getEncoder().withoutPadding();
        return encoder.encodeToString(token.getBytes());
    }

    private String createConfirmationCodeToken(String email, String confirmationCode){
        return jwtUtils.generateJwtConfirmationToken(email, confirmationCode);
    }
}
