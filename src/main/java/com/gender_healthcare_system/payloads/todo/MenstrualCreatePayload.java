package com.gender_healthcare_system.payloads.todo;
import com.gender_healthcare_system.entities.user.Account;
import lombok.Data;
import java.time.LocalDate;

@Data
public class MenstrualCreatePayload {

    private LocalDate startDate;
    private Integer cycleLength;
    private Boolean isTrackingEnabled;
    private Account customer;
}
