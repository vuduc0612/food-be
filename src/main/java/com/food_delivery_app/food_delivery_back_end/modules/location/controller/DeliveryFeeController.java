package com.food_delivery_app.food_delivery_back_end.modules.location.controller;

import com.food_delivery_app.food_delivery_back_end.modules.location.dto.DeliveryFeeRequest;
import com.food_delivery_app.food_delivery_back_end.modules.location.dto.DeliveryFeeResponse;
import com.food_delivery_app.food_delivery_back_end.modules.location.service.DeliveryFeeService;
import com.food_delivery_app.food_delivery_back_end.response.ResponseObject;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.prefix}/delivery")
@RequiredArgsConstructor
@Tag(name = "Delivery API", description = "Provides endpoints for delivery")
public class DeliveryFeeController {

    private final DeliveryFeeService deliveryFeeService;

    @PostMapping("/fee")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Calculate delivery fee", description = "Returns the delivery fee")
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
