package com.gender_healthcare_system.payloads.todo;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Nationalized;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MomoPaymentRefundPayload {

    @Nationalized
    @NotBlank(message = "Customer full name is required")
    @Length(min = 5, max = 70, message = "Customer full name " +
            "must be between 5 and 70 characters")
    private String customerFullName;

    @NotNull(message = "Amount is required")
    @Min(value = 10000, message = "Value must be at least 10,000 VND")
    @Max(value = 20000000, message = "Value must not exceed 20,000,000 VND")
    private Long amount;

    @Nationalized
    @NotBlank(message = "Description is required")
    @Length(min = 5, max = 100, message = "Description " +
            "must be between 5 and 100 characters")
    private String description;

    @NotNull(message = "Transaction ID is required")
    @Min(value = 1000000000L, message = "Transaction ID must be at least 1,000,000,000")
    @Max(value = 9999999999L, message = "Transaction ID must not exceed 9,999,999,999")
    private Long transactionId;

}
