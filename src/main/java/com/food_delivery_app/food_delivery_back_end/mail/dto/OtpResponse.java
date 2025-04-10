package com.food_delivery_app.food_delivery_back_end.mail.dto;

public class OtpResponse {
    private String message;
    private String status;

    public OtpResponse(String message, String status) {
        this.message = message;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}