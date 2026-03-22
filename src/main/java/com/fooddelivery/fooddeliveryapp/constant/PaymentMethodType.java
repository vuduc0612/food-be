package com.fooddelivery.fooddeliveryapp.constant;

public enum PaymentMethodType {
    CASH("Thanh toán bằng tiền mặt"),
    VNPAY("Thanh toán bằng VNPAY");


    private final String displayName;

    PaymentMethodType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

}
