package com.food_delivery_app.food_delivery_back_end.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CustomPageResponse<T> {
    private String message;
    private HttpStatus status;
    private List<T> data;
    private int currentPage;
    private int totalPages;
    private long totalItems;

}