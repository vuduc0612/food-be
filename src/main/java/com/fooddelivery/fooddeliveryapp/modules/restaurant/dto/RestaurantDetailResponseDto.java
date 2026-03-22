package com.fooddelivery.fooddeliveryapp.modules.restaurant.dto;

import com.fooddelivery.fooddeliveryapp.modules.dish.dto.DishResponseDto;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Data
public class RestaurantDetailResponseDto implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Long id;
    private String name;
    private String address;
    private String email;
    private String photoUrl;
    private String phoneNumber;
    private Double longitude;
    private Double latitude;
    private List<DishResponseDto> dishes;
}
