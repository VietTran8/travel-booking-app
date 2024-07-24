package com.web.travel.service.interfaces;

import com.web.travel.dto.ResDTO;
import com.web.travel.dto.request.common.OrderReqDTO;
import com.web.travel.dto.request.common.OrderUpdateReqDTO;
import com.web.travel.dto.response.OrderDetailResDTO;
import com.web.travel.model.Order;
import jakarta.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.List;
import java.util.Map;

public interface OrderService {

    Order saveOrder(Order order);

    List<Order> getAll();

    Order getById(Long id);

    List<OrderDetailResDTO> getByUser(String userEmail);

    ResDTO getOrderResById(Long id);

    Map<String, Object> getAllResponse(int page, int limit);

    ResDTO createPayment(Principal principal, HttpServletRequest request, OrderReqDTO body, boolean isApp) throws UnsupportedEncodingException;

    ResDTO updateOrderStatus(Principal principal, boolean isUserUpdate, OrderUpdateReqDTO dto);

}
