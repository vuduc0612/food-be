package com.food_delivery_app.food_delivery_back_end.modules.payment.controller;

import com.food_delivery_app.food_delivery_back_end.modules.order.entity.Order;
import com.food_delivery_app.food_delivery_back_end.modules.order.service.OrderService;
import com.food_delivery_app.food_delivery_back_end.modules.payment.dto.PaymentDto;
import com.food_delivery_app.food_delivery_back_end.modules.payment.dto.PaymentQueryDto;
import com.food_delivery_app.food_delivery_back_end.modules.payment.dto.PaymentResponse;
import com.food_delivery_app.food_delivery_back_end.modules.payment.service.VnPayService;
import com.food_delivery_app.food_delivery_back_end.response.ResponseObject;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("${api.prefix}/payments")
@RequiredArgsConstructor
public class VnPayController {

    private final VnPayService vnPayService;

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/vnpay")
    public ResponseEntity<?> createPayment(@RequestBody PaymentDto paymentDto, HttpServletRequest request) {
        String paymentUrl = vnPayService.createPaymentUrl(paymentDto, request);
        return ResponseEntity.ok(
                ResponseObject.builder()
                        .status(HttpStatus.OK)
                        .message("Tạo URL thanh toán thành công")
                        .data(paymentUrl)
                        .build()
        );
    }


    @PreAuthorize("hasRole('USER')")
    @GetMapping("/callback-payment")
    public ResponseEntity<?> paymentCallback(HttpServletRequest request) throws Exception {
        // Lấy tất cả các tham số từ VNPAY gửi về
        Map<String, String> vnp_Params = new HashMap<>();
        System.out.println("VNPAY CALLBACK " + request.getQueryString());
        System.out.println("VNPAY CALLBACK2 " + request.getParameterNames());
        Enumeration<String> paramNames = request.getParameterNames();

        while (paramNames.hasMoreElements()) {
            String paramName = paramNames.nextElement();
            String paramValue = request.getParameter(paramName);
            vnp_Params.put(paramName, paramValue);
        }

        // Chuyển việc xử lý sang cho service
        PaymentResponse paymentResponse = vnPayService.processPaymentCallback(vnp_Params);

        return ResponseEntity.ok(paymentResponse);
    }

}
