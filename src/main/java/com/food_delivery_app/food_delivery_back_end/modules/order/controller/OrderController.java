package com.food_delivery_app.food_delivery_back_end.modules.order.controller;

import com.fasterxml.jackson.annotation.JsonIgnoreType;
import com.food_delivery_app.food_delivery_back_end.modules.order.dto.CartDto;
import com.food_delivery_app.food_delivery_back_end.modules.order.dto.CartItem;
import com.food_delivery_app.food_delivery_back_end.modules.order.entity.Cart;
import com.food_delivery_app.food_delivery_back_end.modules.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@Tag(name = "Orders API", description = "Provides endpoints for orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    @PostMapping("/cart/add")
    @Operation(summary = "Add to cart", description = "Returns the cart")
    public ResponseEntity<CartDto> createOrder(
            @RequestParam Long idDish,
            @RequestParam Integer quantity) {
        System.out.println("idDish: " + idDish + " quantity: " + quantity);
        orderService.addToCart(idDish, quantity);

        Cart cart = orderService.getCart();
        System.out.println(cart.getItems().size());
        for(CartItem item : cart.getItems()) {
            System.out.println("Dish: " + item.getName() + " Quantity: " + item.getQuantity());
        }
        if(cart == null) {
            System.out.println("Cart is null");
        }
        System.out.println("Cart is not null");
        // Chuyển Cart thành CartDTO
        CartDto cartDto = new CartDto();
        cartDto.setRestaurantId(cart.getRestaurantId());
        cartDto.setItems(cart.getItems());
        cartDto.setTotalAmount(cart.getTotalAmount());

        return ResponseEntity.ok(cartDto);
    }

    @GetMapping("/cart")
    @Operation(summary = "Get cart", description = "Returns the cart")
    public ResponseEntity<CartDto> getCart(){
        Cart cart = orderService.getCart();
        if(cart == null) {
            System.out.println("Cart is null");
        }
        System.out.println("Cart is not null");
        // Chuyển Cart thành CartDTO
        CartDto cartDto = new CartDto();
        cartDto.setRestaurantId(cart.getRestaurantId());
        cartDto.setItems(cart.getItems());
        cartDto.setTotalAmount(cart.getTotalAmount());

        return ResponseEntity.ok(cartDto);
    }
}
