package com.food_delivery_app.food_delivery_back_end.modules.payment.controller;

import com.food_delivery_app.food_delivery_back_end.modules.payment.dto.PaymentDto;
import com.food_delivery_app.food_delivery_back_end.modules.payment.dto.PaymentResponse;
import com.food_delivery_app.food_delivery_back_end.modules.payment.service.VnPayService;
import com.food_delivery_app.food_delivery_back_end.response.ResponseObject;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("${api.prefix}/payments")
@RequiredArgsConstructor
@Tag(name = "Payments API", description = "Provides endpoints for payments")
public class VnPayController {

    private final VnPayService vnPayService;

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/vnpay")
    @Operation(summary = "Create payment URL", description = "Returns the payment URL")
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
    @Operation(summary = "Payment callback", description = "Handles the payment callback from VNPAY")
    @GetMapping("/callback-payment")
    public ResponseEntity<?> paymentCallback(HttpServletRequest request) throws Exception {
        // Lấy tất cả các tham số từ VNPAY gửi về
        Map<String, String> vnp_Params = new HashMap<>();
        Enumeration<String> paramNames = request.getParameterNames();

        while (paramNames.hasMoreElements()) {
            String paramName = paramNames.nextElement();
            String paramValue = request.getParameter(paramName);
            vnp_Params.put(paramName, paramValue);
        }

        PaymentResponse paymentResponse = vnPayService.processPaymentCallback(vnp_Params);

        return ResponseEntity.ok(paymentResponse);
    }

}
