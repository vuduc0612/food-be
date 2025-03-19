package com.food_delivery_app.food_delivery_back_end.modules.order.controller;

import com.food_delivery_app.food_delivery_back_end.modules.cart.dto.CartDto;
import com.food_delivery_app.food_delivery_back_end.modules.cart.entity.CartItem;
import com.food_delivery_app.food_delivery_back_end.modules.cart.entity.Cart;
import com.food_delivery_app.food_delivery_back_end.modules.order.entity.Order;
import com.food_delivery_app.food_delivery_back_end.modules.order.entity.OrderDetail;
import com.food_delivery_app.food_delivery_back_end.modules.order.response.OrderDetailResponse;
import com.food_delivery_app.food_delivery_back_end.modules.order.response.OrderResponse;
import com.food_delivery_app.food_delivery_back_end.modules.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
@Tag(name = "Orders API", description = "Provides endpoints for orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/cart/add")
    @Operation(summary = "Add to cart", description = "Returns the cart")
    public ResponseEntity<CartDto> addToCart(
            @RequestParam Long idDish,
            @RequestParam Integer quantity) {
        //System.out.println("idDish: " + idDish + " quantity: " + quantity);
        orderService.addToCart(idDish, quantity);

        Cart cart = orderService.getCart();

        if (cart == null) {
            System.out.println("Cart is null");
        }
        System.out.println("Cart is not null");
        // Chuyển Cart thành CartDTO
        CartDto cartDto = new CartDto();
        cartDto.setUserId(cart.getUserId());
        cartDto.setRestaurantId(cart.getRestaurantId());
        cartDto.setItems(cart.getItems());
        cartDto.setTotalAmount(cart.getTotalAmount());

        return ResponseEntity.ok(cartDto);
    }

    @GetMapping("/cart")
    @Operation(summary = "Get cart", description = "Returns the cart")
    public ResponseEntity<CartDto> getCart() {
        Cart cart = orderService.getCart();
        if (cart == null) {
            System.out.println("Cart is null");
        }
        System.out.println("Cart is not null");
        // Chuyển Cart thành CartDTO
        CartDto cartDto = new CartDto();
        cartDto.setUserId(cart.getUserId());
        cartDto.setRestaurantId(cart.getRestaurantId());
        cartDto.setItems(cart.getItems());
        cartDto.setTotalAmount(cart.getTotalAmount());

        return ResponseEntity.ok(cartDto);
    }

    @PostMapping("/place")
    @Operation(summary = "Place order", description = "Returns the order")
    public ResponseEntity<OrderResponse> placeOrder() {
        // get list of order details response from list of order details
        Order order = orderService.placeOrder();
        List<OrderDetailResponse> orderDetailResponses = new ArrayList<>();

        for(OrderDetail orderDetail : order.getOrderDetails()){
            System.out.println("Dish: " + orderDetail.getDish().getName() + " Quantity: " + orderDetail.getQuantity());
            OrderDetailResponse orderDetailResponse = new OrderDetailResponse();
            orderDetailResponse.setId(orderDetail.getId());
            orderDetailResponse.setQuantity(orderDetail.getQuantity());
            orderDetailResponse.setDishName(orderDetail.getDish().getName());
            orderDetailResponses.add(orderDetailResponse);
        }

        //covert order to order response
        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setId(order.getId());
        orderResponse.setStatus(order.getStatus());
        orderResponse.setTotalAmount(order.getTotalAmount());
        orderResponse.setOrderDetailResponses(orderDetailResponses);

        return ResponseEntity.ok(orderResponse);
    }

    @PostMapping("/cart/clear")
    @Operation(summary = "Clear cart", description = "Clears the cart")
    public ResponseEntity<String> clearCart() {
        orderService.clearCart();
        return ResponseEntity.ok("Cart cleared");
    }

    @PostMapping("/cart/remove")
    @Operation(summary = "Remove item from cart", description = "Returns the cart")
    public ResponseEntity<String> removeCart() {
        orderService.removeCart();
        return ResponseEntity.ok("Cart removed");
    }
}
