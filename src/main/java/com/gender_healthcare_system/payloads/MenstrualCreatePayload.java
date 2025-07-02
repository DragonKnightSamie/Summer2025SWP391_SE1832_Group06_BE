package com.gender_healthcare_system.payloads;
import com.gender_healthcare_system.entities.user.Customer;
import lombok.Data;
import java.time.LocalDate;

@Data
public class MenstrualCreatePayload {

    private LocalDate startDate;
    private Integer cycleLength;
    private Boolean isTrackingEnabled;
    private Customer customer;
}
