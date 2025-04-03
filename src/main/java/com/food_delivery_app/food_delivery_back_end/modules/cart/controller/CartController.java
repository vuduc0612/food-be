package com.food_delivery_app.food_delivery_back_end.modules.cart.controller;

import com.food_delivery_app.food_delivery_back_end.constant.AddToCartResultType;
import com.food_delivery_app.food_delivery_back_end.modules.auth.service.AuthService;
import com.food_delivery_app.food_delivery_back_end.modules.cart.dto.AddToCartRequest;
import com.food_delivery_app.food_delivery_back_end.modules.cart.dto.CartDto;
import com.food_delivery_app.food_delivery_back_end.modules.cart.entity.Cart;
import com.food_delivery_app.food_delivery_back_end.modules.cart.service.CartService;
import com.food_delivery_app.food_delivery_back_end.response.ResponseObject;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("${api.prefix}/carts")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;
    private final AuthService authService;
    private final ModelMapper modelMapper;

    //Get cart
    @GetMapping("")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Get cart", description = "Returns the cart")
    public ResponseEntity<ResponseObject> getCart() {
        Long userId = authService.getCurrentUser().getId();
        CartDto cartDto = cartService.getCart(userId);
        return ResponseEntity.ok(
                ResponseObject.builder()
                        .status(HttpStatus.OK)
                        .data(cartDto)
                        .message("Cart retrieved successfully")
                        .build()
        );
    }

    // Add item to cart
    @PostMapping("")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Add to cart", description = "Returns the cart")
    public ResponseEntity<ResponseObject> addToCart(
            @RequestBody AddToCartRequest request) {

        Long userId = authService.getCurrentUser().getId();

        CartDto cartDto = cartService.addToCart(
                userId,
                request.getDishId(),
                request.getQuantity(),
                Boolean.TRUE.equals(request.getForce()) // tránh null
        );

        if (cartDto.getAddToCartResultType() == AddToCartResultType.SUCCESS) {
            return ResponseEntity.ok(
                    ResponseObject.builder()
                            .status(HttpStatus.CREATED)
                            .data(cartDto)
                            .message("Item added to cart successfully")
                            .build()
            );
        } else {
            return ResponseEntity.ok(
                    ResponseObject.builder()
                            .status(HttpStatus.CONFLICT)
                            .data(cartDto)
                            .message("Item not added to cart")
                            .build()
            );
        }
    }

    // Update quantity of item in cart
    @PatchMapping("/dish/{dishId}")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Update quantity cart", description = "Returns the cart")
    public ResponseEntity<ResponseObject> updateCart(
            @PathVariable Long dishId,
            @RequestBody() Map<String, Integer> body) {

        Long userId = authService.getCurrentUser().getId();
        Integer quantity = body.get("quantity");

        cartService.updateItemQuantity(userId, dishId, quantity);

        CartDto cartDto = cartService.getCart(userId);
        return ResponseEntity.ok(
                ResponseObject.builder()
                        .status(HttpStatus.OK)
                        .data(cartDto)
                        .message("Cart updated successfully")
                        .build()
        );
    }

    //Remove item from cart
    @DeleteMapping("/dish/{dishId}")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Remove item from cart", description = "Returns the cart")
    public ResponseEntity<ResponseObject> removeItemFromCart(
            @PathVariable Long dishId) {
        Long userId = authService.getCurrentUser().getId();
        cartService.removeItem(userId, dishId);

        CartDto cartDto = cartService.getCart(userId);

        return ResponseEntity.ok(
                ResponseObject.builder()
                        .status(HttpStatus.OK)
                        .data(cartDto)
                        .message("Item removed from cart successfully")
                        .build()
        );
    }

    //Remove all items from cart
    @DeleteMapping("/all")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Remove from cart", description = "Returns the cart")
    public ResponseEntity<ResponseObject> removeAllItemFromCart() {
        Long userId = authService.getCurrentUser().getId();
        Cart cart = modelMapper.map(cartService.getCart(userId), Cart.class);
        cartService.clearCart(userId, cart);
        return ResponseEntity.ok(
                ResponseObject.builder()
                        .status(HttpStatus.OK)
                        .data(null)
                        .message("Item removed from cart successfully")
                        .build()
        );
    }

    //Clear cart
    @PostMapping("/clear")
    @Operation(summary = "Clear cart", description = "Clears the cart")
    public ResponseEntity<String> clearCart() {
        Long userId = authService.getCurrentUser().getId();
        cartService.removeCart(userId);
        return ResponseEntity.ok("Cart cleared");
    }

}
