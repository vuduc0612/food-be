package com.food_delivery_app.food_delivery_back_end.modules.order.controller;

import com.food_delivery_app.food_delivery_back_end.modules.order.dto.OrderRequestDto;
import com.food_delivery_app.food_delivery_back_end.modules.order.dto.OrderResponse;
import com.food_delivery_app.food_delivery_back_end.modules.order.dto.OrderUpdateRequestDto;
import com.food_delivery_app.food_delivery_back_end.modules.order.service.OrderService;
import com.food_delivery_app.food_delivery_back_end.response.CustomPageResponse;
import com.food_delivery_app.food_delivery_back_end.response.ResponseObject;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/orders")
@Tag(name = "Orders API", description = "Provides endpoints for orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    //Get all order of user
    @GetMapping("users/{user_id}")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Get all orders", description = "Returns all orders")
    public ResponseEntity<CustomPageResponse<OrderResponse>> getAllOrderOfUser(
            @PathVariable("user_id") Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit
    ){
        Page<OrderResponse> orderResponses = orderService.getAllOrderOfUser(userId, page, limit);

        return ResponseEntity.ok(
                CustomPageResponse.<OrderResponse>builder()
                        .message(String.format("Get all orders of user %d successfully", userId))
                        .status(HttpStatus.OK)
                        .data(orderResponses.getContent())
                        .totalPages(orderResponses.getTotalPages())
                        .currentPage(orderResponses.getNumber())
                        .build()
        );
    }

    //Get all order of restaurant
    @GetMapping("restaurants/{restaurant_id}")
    @PreAuthorize("hasRole('RESTAURANT')")
    public ResponseEntity<CustomPageResponse<OrderResponse>> getAllOrderOfRestaurant(
            @PathVariable("restaurant_id") Long restaurantId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit
    ){
        Page<OrderResponse> orderResponses = orderService.getAllOrderOfRestaurant(restaurantId, page, limit);

        return ResponseEntity.ok(
                CustomPageResponse.<OrderResponse>builder()
                        .message(String.format("Get all orders of restaurant %d successfully", restaurantId))
                        .status(HttpStatus.OK)
                        .data(orderResponses.getContent())
                        .totalPages(orderResponses.getTotalPages())
                        .currentPage(orderResponses.getNumber())
                        .build()
        );
    }

    @PostMapping("")
    @Operation(summary = "Place order", description = "Returns the order")
    public ResponseEntity<ResponseObject> placeOrder(@RequestBody  OrderRequestDto orderRequestDto) {

        return ResponseEntity.ok(
                ResponseObject.builder()
                        .message("Place order successfully")
                        .status(HttpStatus.OK)
                        .data(orderService.createOrder(orderRequestDto))
                        .build()
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('RESTAURANT')")
    @Operation(summary = "Update order", description = "Returns the updated order")
    public ResponseEntity<ResponseObject> updateOrder(
            @PathVariable("id") Long id,
            @RequestBody  OrderUpdateRequestDto orderRequestDto
            ) {
        return ResponseEntity.ok(
                ResponseObject.builder()
                        .message("Update order successfully")
                        .status(HttpStatus.OK)
                        .data(orderService.updateOrder(id, orderRequestDto))
                        .build()
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('CUSTOMER')")
    @Operation(summary = "Delete order", description = "Returns the deleted order")
    public ResponseEntity<ResponseObject> deleteOrder(@PathVariable("id") Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.ok(
                ResponseObject.builder()
                        .message("Delete order successfully")
                        .status(HttpStatus.OK)
                        .data(null)
                        .build()
        );
    }

}
