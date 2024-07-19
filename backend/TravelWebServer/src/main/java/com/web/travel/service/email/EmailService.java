package com.web.travel.service.email;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

import com.web.travel.dto.ResDTO;
import com.web.travel.model.Hotel;
import com.web.travel.model.Order;
import com.web.travel.model.Room;
import com.web.travel.model.TourDate;
import com.web.travel.model.enums.EPaymentMethod;
import com.web.travel.model.enums.ERoom;
import com.web.travel.payload.response.MessageResponse;
import com.web.travel.repository.TourBlogRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import com.web.travel.payload.request.MailRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
@Service
public class EmailService {
    @Autowired
    private JavaMailSender sender;
    @Autowired
    private Configuration config;
    @Autowired
    private TourBlogRepository blogRepository;
    @Value("${travel.app.client.host}")
    private String clientHost;
    public ResDTO sendWelcomeEmail(MailRequest request) {
        ResDTO response = new ResDTO();
        response.setData(null);
        MimeMessage message = sender.createMimeMessage();
        Map<String, Object> model = new HashMap<>();
        model.put("name", request.getName());
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());
            //add attachment
            //helper.addAttachment("logo.jpg", new ClassPathResource("logo.jpg"));
            //render template
            Template t = config.getTemplate("email-template.ftl");
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);
            helper.setTo(request.getTo());
            helper.setText(html, true);
            helper.setSubject(request.getSubject());
            helper.setFrom(request.getFrom(), "Travel Vn");
            sender.send(message);
            response.setCode(HttpServletResponse.SC_OK);
            response.setMessage("Mail sent to: " + request.getTo());
            response.setStatus(Boolean.TRUE);
        } catch (MessagingException | IOException | TemplateException e) {
            response.setMessage("Mail sent failure : " + e.getMessage());
            response.setStatus(Boolean.FALSE);
            response.setCode(HttpServletResponse.SC_BAD_REQUEST);
        }
        return response;
    }

    public ResDTO sendResetPasswordEmail(MailRequest request, Map<String, Object> model) {
        ResDTO response = new ResDTO();
        response.setData(null);
        MimeMessage message = sender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());
            //add attachment
            //helper.addAttachment("logo.jpg", new ClassPathResource("logo.jpg"));
            //render template
            Template t = config.getTemplate("reset-password/reset-password-template.html");
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);
            helper.setTo(request.getTo());
            helper.setText(html, true);
            helper.setSubject(request.getSubject());
            helper.setFrom(request.getFrom(), "Travel Vn");
            sender.send(message);
            response.setCode(HttpServletResponse.SC_OK);
            response.setMessage("Mail sent to: " + request.getTo());
            response.setStatus(Boolean.TRUE);
        } catch (MessagingException | IOException | TemplateException e) {
            response.setMessage("Mail sent failure : " + e.getMessage());
            response.setStatus(Boolean.FALSE);
            response.setCode(HttpServletResponse.SC_BAD_REQUEST);
        }
        return response;
    }

    @Async
    public void sendConfirmationEmail(String email, String userFullName, String token, String confirmationCode){
        MessageResponse response = new MessageResponse();

        MailRequest request = new MailRequest();
        request.setFrom("travel-vn");
        request.setSubject("ACCOUNT CONFIRMATION");
        request.setTo(email);

        Map<String, Object> model = new HashMap<>();
        model.put("name", userFullName);
        model.put("clientHost", clientHost);

        String[] confirmationCodeSplit = confirmationCode.split("");
        for(int i = 0; i < confirmationCodeSplit.length; i++){
            model.put("digit" + String.valueOf(i), confirmationCodeSplit[i]);
        }

        MimeMessage message = sender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());
            //add attachment
            //helper.addAttachment("logo.jpg", new ClassPathResource("logo.jpg"));
            //render template
            Template t = config.getTemplate("confirmation/confirmation-template.html");
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);
            helper.setTo(request.getTo());
            helper.setText(html, true);
            helper.setSubject(request.getSubject());
            helper.setFrom(request.getFrom(), "Travel Vn");
            sender.send(message);
            response.setMessage("Mail sent to: " + request.getTo());
            response.setStatus(Boolean.TRUE);

            Map<String, String> tokenDTO = new HashMap<>();
            tokenDTO.put("token", token);
            response.setData(tokenDTO);

        } catch (MessagingException | IOException | TemplateException e) {
            response.setMessage("Mail sent failure : " + e.getMessage());
            response.setStatus(Boolean.FALSE);
        }
    }


    @Async
    public void sendOrderedEmail(Order order, boolean isOnline){
        MessageResponse response = new MessageResponse();

        MailRequest request = new MailRequest();
        request.setFrom("travel-vn");
        request.setSubject("YOUR ORDER");
        request.setTo(order.getContactInfo().getCustomerEmail());

        Map<String, Object> model = new HashMap<>();
        model.put("name", order.getContactInfo().getCustomerFullName());
        model.put("orderId", order.getId());

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        model.put("orderDate", formatter.format(order.getOrderDate()));
        model.put("phone", order.getContactInfo().getCustomerPhone());
        model.put("email", order.getContactInfo().getCustomerEmail());
        model.put("address", order.getContactInfo().getCustomerAddress());
        model.put("amount", order.getTotalPrice());
        model.put("adults", order.getAdults());
        model.put("specialRequest", order.getSpecialRequest() != null ? order.getSpecialRequest() : "Không");
        model.put("children", order.getChildren());

        TourDate tourDate = order.getTourDate();
        model.put("adultPrice", tourDate.getAdultPrice());
        model.put("childrenPrice", tourDate.getChildPrice());
        Hotel orderHotel = order.getHotel();
        model.put("hotelName", "null");
        model.put("hotelImg", "null");
        model.put("hotelPrice", 0);
        if(orderHotel != null){
            model.put("hotelName", order.getHotel().getName());
            model.put("hotelImg", orderHotel.getIllustration() == null ? "" : orderHotel.getIllustration());
            Room room = orderHotel.getRooms().stream().filter(
                    item -> item.getType().equals(ERoom.valueOf("TYPE_" + (order.getRoomType() != null ? order.getRoomType() : "normal").toUpperCase()))
            ).findFirst().orElse(null);

            model.put("hotelPrice",
                    room != null ? order.getTotalPeople() * room.getPrice() : 0
            );
        }

        model.put("tourImage", order.getTour().getImg());
        model.put("tourName", order.getTour().getName());
        model.put("departDate", formatter.format(order.getTourDate().getDepartDate()).split(" ")[0]);
        model.put("endDate", formatter.format(order.getTourDate().getEndDate()).split(" ")[0]);
        model.put("depart", order.getTour().getDepart());
        model.put("destination", order.getTour().getDestination());

        EPaymentMethod method = order.getPaymentMethod();
        if(method == EPaymentMethod.METHOD_VNPAY){
            model.put("method", "Thanh toán bằng VNPay");
        }else if(method == EPaymentMethod.METHOD_PAYPAL){
            model.put("method", "Thanh toán bằng PayPal");
        }else{
            model.put("method", "Thanh toán bằng tiền mặt");
        }
        model.put("clientHost", clientHost);
        MimeMessage message = sender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());
            //add attachment
            //helper.addAttachment("logo.jpg", new ClassPathResource("logo.jpg"));
            //render template

            Template t = config.getTemplate("ordered/unordered-template.html");
            if(isOnline)
                t = config.getTemplate("ordered/ordered-template.html");
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);
            helper.setTo(request.getTo());
            helper.setText(html, true);
            helper.setSubject(request.getSubject());
            helper.setFrom(request.getFrom(), "Travel Vn");
            sender.send(message);
            response.setMessage("Mail sent to: " + request.getTo());
            response.setStatus(Boolean.TRUE);

        } catch (MessagingException | IOException | TemplateException e) {
            response.setMessage("Mail sent failure : " + e.getMessage());
            response.setStatus(Boolean.FALSE);
        }
    }

    @Async
    public void sendCanceledEmail(Order order){
        MessageResponse response = new MessageResponse();

        MailRequest request = new MailRequest();
        request.setFrom("travel-vn");
        request.setSubject("YOUR ORDER IS CANCELED");
        request.setTo(order.getContactInfo().getCustomerEmail());

        Map<String, Object> model = new HashMap<>();

        model.put("orderId", order.getId());
        model.put("name", order.getContactInfo().getCustomerFullName());
        model.put("clientHost", clientHost);
        MimeMessage message = sender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());
            //add attachment
            //helper.addAttachment("logo.jpg", new ClassPathResource("logo.jpg"));
            //render template
            Template t = config.getTemplate("ordered/canceled-template.html");
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);
            helper.setTo(request.getTo());
            helper.setText(html, true);
            helper.setSubject(request.getSubject());
            helper.setFrom(request.getFrom(), "Travel Vn");
            sender.send(message);
            response.setMessage("Mail sent to: " + request.getTo());
            response.setStatus(Boolean.TRUE);

        } catch (MessagingException | IOException | TemplateException e) {
            response.setMessage("Mail sent failure : " + e.getMessage());
            response.setStatus(Boolean.FALSE);
        }
    }
}