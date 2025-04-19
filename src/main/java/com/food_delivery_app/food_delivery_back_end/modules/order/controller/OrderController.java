package com.food_delivery_app.food_delivery_back_end.modules.order.controller;

import com.food_delivery_app.food_delivery_back_end.constant.OrderStatusType;
import com.food_delivery_app.food_delivery_back_end.modules.auth.service.AuthService;
import com.food_delivery_app.food_delivery_back_end.modules.order.dto.OrderRequestDto;
import com.food_delivery_app.food_delivery_back_end.modules.order.dto.OrderResponseDto;
import com.food_delivery_app.food_delivery_back_end.modules.order.dto.OrderUpdateRequestDto;
import com.food_delivery_app.food_delivery_back_end.modules.order.service.OrderService;
import com.food_delivery_app.food_delivery_back_end.response.CustomPageResponse;
import com.food_delivery_app.food_delivery_back_end.response.ResponseObject;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
    private final AuthService authSevice;

    //Get all order of user
    @GetMapping("users/{user_id}")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Get all orders", description = "Returns all orders")
    public ResponseEntity<CustomPageResponse<OrderResponseDto>> getAllOrderOfUser(
            @PathVariable("user_id") Long userId,
            @RequestParam(defaultValue = "DELIVERED") String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit
    ) {
        OrderStatusType orderStatusType = OrderStatusType.valueOf(status.toUpperCase());
        Page<OrderResponseDto> orderResponses = orderService.getAllOrderOfUser(userId, orderStatusType, page, limit);

        return ResponseEntity.ok(
                CustomPageResponse.<OrderResponseDto>builder()
                        .message(String.format("Get all orders of user %d successfully", userId))
                        .status(HttpStatus.OK)
                        .data(orderResponses.getContent())
                        .totalPages(orderResponses.getTotalPages())
                        .currentPage(orderResponses.getNumber())
                        .build()
        );
    }

    //Get all order of current user
    @GetMapping("users/me")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Get all orders", description = "Returns all orders")
    public ResponseEntity<CustomPageResponse<OrderResponseDto>> getAllOrderOfUserCurrent(
            @RequestParam(defaultValue = "DELIVERED") String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit
    ) {
        Long userId = authSevice.getCurrentUser().getId();
        OrderStatusType orderStatusType = OrderStatusType.valueOf(status.toUpperCase());
        Page<OrderResponseDto> orderResponses = orderService.getAllOrderOfUser(userId, orderStatusType, page, limit);

        return ResponseEntity.ok(
                CustomPageResponse.<OrderResponseDto>builder()
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
    public ResponseEntity<CustomPageResponse<OrderResponseDto>> getAllOrderOfRestaurant(
            @PathVariable("restaurant_id") Long restaurantId,
            @RequestParam(defaultValue = "") String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit
    ) {
        OrderStatusType orderStatusType;
        if (status.equals("null")) {
            orderStatusType = null;
        } else {
            orderStatusType = OrderStatusType.valueOf(status.toUpperCase());
        }
        Page<OrderResponseDto> orderResponses = orderService.getAllOrderOfRestaurant(restaurantId, orderStatusType, page, limit);

        return ResponseEntity.ok(
                CustomPageResponse.<OrderResponseDto>builder()
                        .message(String.format("Get all orders of restaurant %d successfully", restaurantId))
                        .status(HttpStatus.OK)
                        .data(orderResponses.getContent())
                        .totalPages(orderResponses.getTotalPages())
                        .currentPage(orderResponses.getNumber())
                        .build()
        );
    }

    //Get all order of current restaurant
    @GetMapping("restaurants/me")
    @PreAuthorize("hasRole('RESTAURANT')")
    @Operation(summary = "Get all orders", description = "Returns all orders")
    public ResponseEntity<CustomPageResponse<OrderResponseDto>> getAllOrderOfRestaurantCurrent(
            @RequestParam(defaultValue = "null") String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit
    ) {
        Long restaurantId = authSevice.getCurrentRestaurant().getId();
        OrderStatusType orderStatusType;
        if (status.equals("null")) {
            orderStatusType = null;
        } else {
            orderStatusType = OrderStatusType.valueOf(status.toUpperCase());
        }
        Page<OrderResponseDto> orderResponses = orderService.getAllOrderOfRestaurant(restaurantId, orderStatusType, page, limit);

        return ResponseEntity.ok(
                CustomPageResponse.<OrderResponseDto>builder()
                        .message(String.format("Get all orders of restaurant %d successfully", restaurantId))
                        .status(HttpStatus.OK)
                        .data(orderResponses.getContent())
                        .totalPages(orderResponses.getTotalPages())
                        .currentPage(orderResponses.getNumber())
                        .build()
        );
    }

    //Get order by id
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'RESTAURANT')")
    @Operation(summary = "Get order", description = "Returns the order")
    public ResponseEntity<ResponseObject> getOrder(@PathVariable("id") Long id) {
        OrderResponseDto orderResponseDto = orderService.getOrder(id);
        return ResponseEntity.ok(
                ResponseObject.builder()
                        .message("Get order successfully")
                        .status(HttpStatus.OK)
                        .data(orderResponseDto)
                        .build()
        );
    }

    //Place order
    @PostMapping("")
    @PreAuthorize("hasRole('CUSTOMER')")
    @Operation(summary = "Place order", description = "Returns the order")
    public ResponseEntity<ResponseObject> placeOrder(@RequestBody OrderRequestDto orderRequestDto) {

        return ResponseEntity.ok(
                ResponseObject.builder()
                        .message("Place order successfully")
                        .status(HttpStatus.OK)
                        .data(orderService.createOrder(orderRequestDto))
                        .build()
        );
    }

    //Update order
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('RESTAURANT')")
    @Operation(summary = "Update order", description = "Returns the updated order")
    public ResponseEntity<ResponseObject> updateOrder(
            @PathVariable("id") Long id,
            @RequestBody OrderUpdateRequestDto orderRequestDto
    ) {
        return ResponseEntity.ok(
                ResponseObject.builder()
                        .message("Update order successfully")
                        .status(HttpStatus.OK)
                        .data(orderService.updateOrder(id, orderRequestDto))
                        .build()
        );
    }

    //Delete order
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
