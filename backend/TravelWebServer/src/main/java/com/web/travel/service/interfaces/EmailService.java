package com.web.travel.service.interfaces;

import com.web.travel.dto.ResDTO;
import com.web.travel.model.Order;
import com.web.travel.payload.request.MailRequest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface EmailService {

    ResDTO sendWelcomeEmail(MailRequest request);

    ResDTO sendResetPasswordEmail(MailRequest request, Map<String, Object> model);

    void sendConfirmationEmail(String email, String userFullName, String token, String confirmationCode);

    void sendOrderedEmail(Order order, boolean isOnline);

    void sendCanceledEmail(Order order);
}
