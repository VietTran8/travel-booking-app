package com.web.travel.controller.admin;

import com.web.travel.dto.ResDTO;
import com.web.travel.dto.request.common.OrderUpdateReqDTO;
import com.web.travel.service.impl.EmailServiceImpl;
import com.web.travel.service.interfaces.EmailService;
import com.web.travel.service.interfaces.OrderService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/admin/order")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class OrderAdminController {
    private final OrderService orderService;
    private final EmailService emailService;

    @PostMapping("/update")
    @CrossOrigin(origins = "*")
    public ResponseEntity<?> updateOrderStatus(Principal principal,  @RequestBody OrderUpdateReqDTO orderDto){
        ResDTO response = orderService.updateOrderStatus(principal, false, orderDto);
        if(response.isStatus())
            return ResponseEntity.ok().body(response);
        return ResponseEntity.badRequest().body(response);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllOrder(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit
    ){
        return ResponseEntity.ok().body(
                new ResDTO(
                    HttpServletResponse.SC_OK,
                    true,
                    "Orders fetched successfully!",
                    orderService.getAllResponse(page, limit)
                )
        );
    }
}
