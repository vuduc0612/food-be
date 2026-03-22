package com.fooddelivery.fooddeliveryapp.modules.category.dto;

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
