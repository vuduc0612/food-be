package com.food_delivery_app.food_delivery_back_end.modules.location.controller;

import com.food_delivery_app.food_delivery_back_end.modules.location.dto.DeliveryFeeRequest;
import com.food_delivery_app.food_delivery_back_end.modules.location.dto.DeliveryFeeResponse;
import com.food_delivery_app.food_delivery_back_end.modules.location.service.DeliveryFeeService;
import com.food_delivery_app.food_delivery_back_end.response.ResponseObject;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.prefix}/delivery")
@RequiredArgsConstructor
public class DeliveryFeeController {

    private final DeliveryFeeService deliveryFeeService;

    @PostMapping("/fee")
    public ResponseEntity<?> calculateFee(@RequestBody DeliveryFeeRequest request) {
        DeliveryFeeResponse deliveryFeeResponse = deliveryFeeService.calculateFee(request.getFrom(), request.getTo());
        return ResponseEntity.ok(
                ResponseObject.builder()
                        .status(HttpStatus.OK)
                        .message("Tính phí giao hàng thành công")
                        .data(deliveryFeeResponse)
                        .build()
        );
    }
}
