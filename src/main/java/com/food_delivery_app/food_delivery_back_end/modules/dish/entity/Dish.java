package com.food_delivery_app.food_delivery_back_end.modules.dish.entity;

import com.food_delivery_app.food_delivery_back_end.modules.category.entity.Category;
import com.food_delivery_app.food_delivery_back_end.modules.restaurant.entity.Restaurant;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "dishes")
@Setter
@Getter
@Builder
public class Dish {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;
    private String thumbnail;
    private Double price;
    private Double discount;
    private Boolean isAvailable;

    @ManyToOne
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @ManyToOne()
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
}
