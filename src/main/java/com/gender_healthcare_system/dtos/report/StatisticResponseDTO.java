package com.gender_healthcare_system.dtos.report;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;


@Data
@NoArgsConstructor
public class StatisticResponseDTO implements Serializable {

    private Date date;

    private Long totalCount;

    private Long totalAmount;

    public StatisticResponseDTO(Date date, Long totalCount, Long totalAmount) {
        this.date = date;
        this.totalCount = totalCount;
        this.totalAmount = totalAmount;
    }
}
