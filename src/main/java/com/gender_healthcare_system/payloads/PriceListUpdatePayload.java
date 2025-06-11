package com.gender_healthcare_system.payloads;

import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public class PriceListUpdatePayload {

    @NotNull
    private int priceId; // ID của bảng giá, dùng để cập nhật

    @NotNull
    @Length(min = 5, max = 255, message = "Description must be between 5 and 255 characters")
    private String description; // Mô tả bảng giá

    @NotNull(message = "Price is required")
    private double price; // Giá tiền


}
