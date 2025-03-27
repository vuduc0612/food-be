package com.food_delivery_app.food_delivery_back_end.modules.category.dto;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CategoryDto {
    private Long id;
    private String name;
}
