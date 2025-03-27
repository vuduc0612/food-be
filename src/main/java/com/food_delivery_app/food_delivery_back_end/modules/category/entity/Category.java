package com.food_delivery_app.food_delivery_back_end.modules.category.entity;

import com.food_delivery_app.food_delivery_back_end.modules.restaurant.dto.RestaurantDto;
import com.food_delivery_app.food_delivery_back_end.modules.restaurant.entity.Restaurant;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

}
