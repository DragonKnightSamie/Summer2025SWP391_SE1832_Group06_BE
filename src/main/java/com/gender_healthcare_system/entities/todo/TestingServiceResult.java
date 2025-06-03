package com.gender_healthcare_system.entities.todo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Nationalized;

@Entity
@Table(name = "TestingServiceResult")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestingServiceResult {

    @Id
    @Column(name = "service_result_id")
    private int serviceResultId;

    @Nationalized
    @Column(name = "content")
    private String content;

    @Column(name = "description")
    private String description;



}
