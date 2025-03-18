package com.food_delivery_app.food_delivery_back_end.constant;

public enum OrderStatusType {
    PENDING,       // Order placed, waiting for restaurant confirmation
    CONFIRMED,     // Restaurant confirmed the order
    PREPARING,     // Restaurant is preparing the food
    READY,         // Food is ready for pickup
    OUT_FOR_DELIVERY, // Driver has picked up and is delivering
    DELIVERED,     // Order delivered successfully
    CANCELLED,     // Order cancelled by user
    REJECTED       // Order rejected by restaurant
}
