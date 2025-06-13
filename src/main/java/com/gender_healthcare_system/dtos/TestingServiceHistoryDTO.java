package com.gender_healthcare_system.dtos;

import com.gender_healthcare_system.entities.enu.Rating;
import com.gender_healthcare_system.entities.enu.TestingServiceHistoryStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class TestingServiceHistoryDTO {

    /* ====== CÁC TRƯỜNG CHÍNH ====== */
    private int serviceHistoryId;
    private String result;
    private Rating rating;
    private String comment;
    private LocalDateTime createdAt;
    private TestingServiceHistoryStatus status;

    private TestingServiceDTO testingService;
    private StaffDTO staff;
    private CustomerDTO customer;
    private PaymentDTO payment;


    public TestingServiceHistoryDTO(int serviceHistoryId,
                                    String result,
                                    Rating rating,
                                    String comment,
                                    LocalDateTime createdAt,
                                    TestingServiceHistoryStatus status,
                                    TestingServiceDTO testingService,
                                    StaffDTO staff,
                                    CustomerDTO customer,
                                    PaymentDTO payment) {
        this.serviceHistoryId = serviceHistoryId;
        this.result = result;
        this.rating = rating;
        this.comment = comment;
        this.createdAt = createdAt;
        this.status = status;
        this.testingService = testingService;
        this.staff = staff;
        this.customer = customer;
        this.payment = payment;
    }


    public TestingServiceHistoryDTO(int serviceHistoryId,
                                    String result,
                                    Rating rating,
                                    String comment,
                                    LocalDateTime createdAt,
                                    TestingServiceHistoryStatus status) {
        this.serviceHistoryId = serviceHistoryId;
        this.result = result;
        this.rating = rating;
        this.comment = comment;
        this.createdAt = createdAt;
        this.status = status;
    }
}
