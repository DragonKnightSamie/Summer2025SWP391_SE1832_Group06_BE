package com.gender_healthcare_system.dtos.report;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@NoArgsConstructor
public class StatisticResponseDTO implements Serializable {

    private LocalDate date;

    private Long totalCount;

    private Long totalAmount;

    public StatisticResponseDTO(Date date, Long totalCount, Long totalAmount) {
        Instant instant = date.toInstant();
        ZoneId zone = ZoneId.of("Asia/Bangkok");
        this.date = instant.atZone(zone).toLocalDate();
        this.totalCount = totalCount;
        this.totalAmount = totalAmount;
    }
}
