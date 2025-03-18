package com.food_delivery_app.food_delivery_back_end.modules.order.entity;

import com.food_delivery_app.food_delivery_back_end.modules.dish.repository.DishRepository;
import com.food_delivery_app.food_delivery_back_end.modules.order.dto.CartItem;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

@Component
@Scope(value = "prototype")
@Getter
@Setter
public class Cart {
    private Long userId;
    private Long restaurantId;
    private List<CartItem> items = new ArrayList<>();
    private Double totalAmount = 0.0;
    private DishRepository dishRepository;
    public void addItem(CartItem item){
        if(items.isEmpty()){
            restaurantId = item.getRestaurantId();
        }
        else if(!restaurantId.equals(item.getRestaurantId())){
            throw new IllegalArgumentException("Cart can only contain items from one restaurant");
        }
        boolean itemExists = false;
        for(CartItem existingItem : items){
            if(existingItem.getIdDish().equals(item.getIdDish())){
                existingItem.setQuantity(existingItem.getQuantity() + item.getQuantity());
                itemExists = true;
                break;
            }
        }
        if(!itemExists){
            items.add(item);
        }
        updateTotalAmount();
    }
    public void updateItemQuantity(Long idDish, Integer quantity){
        for(CartItem item : items){
            if(item.getIdDish().equals(idDish)){
                item.setQuantity(quantity);
                break;
            }
        }
    }
    public void removeItem(Long idDish){
        items.removeIf(item -> item.getIdDish().equals(idDish));
    }

    public void clear(){
        items.clear();
        totalAmount = 0.0;
    }

    public void updateTotalAmount(){
        totalAmount = items.stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
    }
}
