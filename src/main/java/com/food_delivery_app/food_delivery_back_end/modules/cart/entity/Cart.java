package com.food_delivery_app.food_delivery_back_end.modules.cart.entity;

import com.food_delivery_app.food_delivery_back_end.modules.dish.repository.DishRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Scope(value = "prototype")
@Getter
@Setter
public class Cart {

    private Long restaurantId;
    private List<CartItem> items = new ArrayList<>();
    private Double totalAmount = 0.0;
    private DishRepository dishRepository;

    public void updateTotalAmount(){
        totalAmount = items.stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
    }
}
