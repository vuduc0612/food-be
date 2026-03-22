package com.fooddelivery.fooddeliveryapp.modules.dish.entity;

import com.fooddelivery.fooddeliveryapp.constant.DishStatusType;
import com.fooddelivery.fooddeliveryapp.modules.category.entity.Category;
import com.fooddelivery.fooddeliveryapp.modules.restaurant.entity.Restaurant;
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
    private DishStatusType isAvailable;


    @ManyToOne
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @ManyToOne()
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
}
