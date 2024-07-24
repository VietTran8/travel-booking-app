package com.web.travel.controller;

import com.web.travel.dto.ResDTO;
import com.web.travel.dto.request.common.OrderUpdateReqDTO;
import com.web.travel.service.impl.OrderServiceImpl;
import com.web.travel.service.interfaces.OrderService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/order")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    @GetMapping
    public ResponseEntity<?> getOrdersByUser(Principal principal){
        return ResponseEntity.ok(
            new ResDTO(
                HttpServletResponse.SC_OK,
                true,
                "Order fetched successfully",
                orderService.getByUser(principal.getName())
            )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable Long id){
        ResDTO response = orderService.getOrderResById(id);
        return response.isStatus() ? ResponseEntity.ok(response) :
                ResponseEntity.badRequest().body(response);
    }

    @PostMapping("/cancel/{id}")
    public ResponseEntity<?> cancelOrder(Principal principal, @PathVariable long id){

        OrderUpdateReqDTO request = new OrderUpdateReqDTO();
        request.setId(id);
        request.setStatus("canceled");

        ResDTO response = orderService.updateOrderStatus(principal, true, request);
        return response.isStatus() ? ResponseEntity.ok(
                response
        ) : ResponseEntity.badRequest().body(response);
    }
}
