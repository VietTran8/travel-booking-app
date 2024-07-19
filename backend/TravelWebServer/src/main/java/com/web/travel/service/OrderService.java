package com.web.travel.service;

import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import com.web.travel.dto.ResDTO;
import com.web.travel.dto.request.common.OrderReqDTO;
import com.web.travel.dto.request.common.OrderUpdateReqDTO;
import com.web.travel.dto.response.OrderDetailResDTO;
import com.web.travel.mapper.Mapper;
import com.web.travel.mapper.request.OrderReqMapper;
import com.web.travel.mapper.response.OrderResMapper;
import com.web.travel.model.ContactInfo;
import com.web.travel.model.Order;
import com.web.travel.model.TourDate;
import com.web.travel.model.User;
import com.web.travel.model.enums.EOrderStatus;
import com.web.travel.repository.OrderRepository;
import com.web.travel.repository.TourDateRepository;
import com.web.travel.service.email.EmailService;
import com.web.travel.service.paypal.PaypalService;
import com.web.travel.service.vnpay.VnPayService;
import com.web.travel.utils.DateHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    UserService userService;
    @Autowired
    EmailService emailService;
    @Autowired
    TourDateRepository tourDateRepository;
    @Autowired
    VnPayService vnPayService;
    @Autowired
    PaypalService paypalService;
    @Autowired
    OrderReqMapper orderReqMapper;
    @Autowired
    ContactInfoService contactInfoService;
    @Autowired
    TourService tourService;

    public Order saveOrder(Order order){
        return orderRepository.save(order);
    }
    public List<Order> getAll(){
        return orderRepository.findAll();
    }
    public Order getById(Long id){
        return orderRepository.findById(id).orElse(null);
    }
    public List<OrderDetailResDTO> getByUser(String userEmail){
        User user = userService.getUserObjectByEmail(userEmail);
        List<Order> orders = orderRepository.findByUserOrderByOrderDateDesc(user);

        return orders.stream().map(order -> {
            OrderResMapper mapper = new OrderResMapper();
            return (OrderDetailResDTO) mapper.mapToDTO(order);
        }).collect(Collectors.toList());
    }

    public ResDTO getOrderResById(Long id){
        Order foundOrder = orderRepository.findById(id).orElse(null);
        OrderDetailResDTO responseData = null;
        int statusCode = HttpServletResponse.SC_BAD_REQUEST;
        boolean status = false;
        String message = "Không tìm thấy đơn hàng có mã: " + id;
        if(foundOrder != null){
            Mapper mapper = new OrderResMapper();
            responseData = (OrderDetailResDTO) mapper.mapToDTO(foundOrder);
            statusCode = HttpServletResponse.SC_OK;
            status = true;
            message = "Lấy đơn hàng thành công!";
        }
        return new ResDTO(
                statusCode,
                status,
                message,
                responseData
        );
    }

    public Map<String, Object> getAllResponse(int page, int limit){
        Page<Order> orders = orderRepository.findAllByOrderByOrderDateDesc(PageRequest.of(page - 1, limit));

        Map<String, Object> response = new HashMap<>();

        List<OrderDetailResDTO> ordersResponse = orders.get().map(order -> {
            OrderResMapper mapper = new OrderResMapper();
            return (OrderDetailResDTO) mapper.mapToDTO(order);
        }).toList();

        response.put("pages", orders.getTotalPages());
        response.put("orders", ordersResponse);

        return response;
    }

    public ResDTO createPayment(Principal principal, HttpServletRequest request, @RequestBody OrderReqDTO body, boolean isApp) throws UnsupportedEncodingException {
        long amount = (long) Math.round(body.getAmount());
        String ipAddress = request.getRemoteAddr();

        Order order = (Order) orderReqMapper.mapToObject(body);
        order.setTotalPrice(body.getAmount());

        TourDate orderTourDate = order.getTourDate();
        if(orderTourDate != null){
            if(orderTourDate.getDepartDate().getTime() >= order.getOrderDate().getTime()) {
                if (orderTourDate.canBook(order.getAdults())) {
                    if (principal != null)
                        order.setUser(userService.getUserObjectByEmail(principal.getName()));

                    int currentPeople = orderTourDate.getCurrentPeople();
                    orderTourDate.setCurrentPeople(currentPeople + order.getTotalPeople());

                    TourDate savedTourDate = tourDateRepository.save(orderTourDate);

                    ContactInfo contactInfo = contactInfoService.saveContactInfo(order.getContactInfo());
                    Long contactInfoId = contactInfo.getId();
                    order.getContactInfo().setId(contactInfoId);

                    Order savedOrder = saveOrder(order);

                    long orderId = savedOrder.getId();
                    ResDTO response = new ResDTO(
                            HttpServletResponse.SC_OK,
                            true,
                            "Đặt tour thành công!",
                            orderId
                    );

                    String paymentMethod = body.getPaymentMethod();
                    if (paymentMethod.equals("vnpay")) {
                        HashMap<String, Long> idParams = new HashMap<>();
                        idParams.put("orderId", orderId);
                        idParams.put("tourDateId", savedTourDate.getId());
                        idParams.put("tourId", savedTourDate.getTour().getId());

                        response = vnPayService.createPayment(amount, ipAddress, idParams, body.getSessionToken(), isApp);

                    }else if(paymentMethod.equals("paypal")){
                        Payment payment = null;
                        try {
                            HashMap<String, Long> idParams = new HashMap<>();
                            idParams.put("orderId", orderId);
                            idParams.put("tourDateId", savedTourDate.getId());
                            idParams.put("tourId", savedTourDate.getTour().getId());

                            payment = paypalService.createPayment(convertVNDtoUSD(amount), idParams, body.getSessionToken(), isApp);

                        } catch (PayPalRESTException e) {
                            return new ResDTO(
                                HttpServletResponse.SC_BAD_REQUEST,
                                true,
                                "Có lỗi xảy ra, vui lòng thử lại sau!",
                                null
                            );
                        }
                        for(Links link : payment.getLinks()) {
                            if(link.getRel().equals("approval_url")) {
                                return new ResDTO(
                                    HttpServletResponse.SC_OK,
                                    true,
                                    "Đặt tour thành công!",
                                    link.getHref()
                                );
                            }
                        }
                    } else {
                        emailService.sendOrderedEmail(order, false);
                    }

                    return response;
                }
                return new ResDTO(
                        HttpServletResponse.SC_BAD_REQUEST,
                        false,
                        "Tour đã đầy!",
                        null
                );
            }
            return new ResDTO(
                    HttpServletResponse.SC_BAD_REQUEST,
                    false,
                    "Ngày này đã đi rồi!",
                    null
            );
        }
        return new ResDTO(
                HttpServletResponse.SC_BAD_REQUEST,
                false,
                "Vui lòng nhập mã ngày tour!",
                null
        );
    }

    public ResDTO updateOrderStatus(Principal principal, boolean isUserUpdate, OrderUpdateReqDTO dto){
        Order order = getById(dto.getId());
        if(order != null){
            EOrderStatus needUpdateStatus = EOrderStatus.valueOf("STATUS_" + dto.getStatus().toUpperCase());

            //The needed update status must be different with the exists one
            if(order.getStatus() != needUpdateStatus) {

                Map<String, Object> response = new HashMap<>();
                response.put("id", order.getId());
                response.put("status", order.getStatus());

                //Get number of days from updated date to departure date
                DateHandler dateHandler = new DateHandler();
                int diffDay = dateHandler
                        .getDiffDay(order.getTourDate().getDepartDate(), DateHandler.getCurrentDateTime());

                //Ticket booking can only be canceled 3 days before departure date
                if(needUpdateStatus == EOrderStatus.STATUS_CANCELED){
                    if(diffDay >= 3){
                        if(!isUserUpdate || principal.getName().equals(order.getUser().getEmail())){
                            order.setStatus(needUpdateStatus);

                            //Reduce current people due to this order is canceled
                            int currentPeople = order.getTourDate().getCurrentPeople();
                            order.getTourDate().setCurrentPeople(currentPeople - order.getTotalPeople());
                            tourDateRepository.save(order.getTourDate());

                            saveOrder(order);
                            response.put("status", order.getStatus());

                            emailService.sendCanceledEmail(order);
                            return new ResDTO(
                                    HttpServletResponse.SC_OK,
                                    true,
                                    "Hủy đặt tour thành công!",
                                    response
                            );
                        }
                        return new ResDTO(
                                HttpServletResponse.SC_BAD_REQUEST,
                                false,
                                "Không thể hủy tour của người khác!",
                                response
                        );
                    }
                    return new ResDTO(
                        HttpServletResponse.SC_BAD_REQUEST,
                        false,
                        "Chỉ có thể hủy đặt tour trước 3 ngày trở lên!",
                        response
                    );
                }
                //Ordered status can only be updated from Pending status and vice versa
                if(order.getStatus() != EOrderStatus.STATUS_CANCELED){
                    order.setStatus(needUpdateStatus);
                    saveOrder(order);
                    response.put("status", order.getStatus());

                    if(needUpdateStatus == EOrderStatus.STATUS_ORDERED){
                        emailService.sendOrderedEmail(getById(order.getId()), true);
                    }
                    return new ResDTO(
                        HttpServletResponse.SC_OK,
                        true,
                        "Cập nhật trạng thái thành công!",
                        response
                    );
                }else{
                    return new ResDTO(
                            HttpServletResponse.SC_BAD_REQUEST,
                            false,
                            "Không thể cập nhật trạng thái khi đã hủy!",
                            response
                    );
                }
            }
            return new ResDTO(
                    HttpServletResponse.SC_BAD_REQUEST,
                    false,
                    "Trạng thái không thay đổi!",
                    null
            );
        }
        return new ResDTO(
            HttpServletResponse.SC_BAD_REQUEST,
            false,
            "Không tìm thấy đơn hàng có mã: " + dto.getId(),
            null
        );
    }

    public static double convertVNDtoUSD(long vndAmount) {
        return vndAmount * 0.000041;
    }
}
