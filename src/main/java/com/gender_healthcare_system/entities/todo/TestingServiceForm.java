package com.gender_healthcare_system.entities.todo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "TestingServiceForm")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestingServiceForm {

    @Id
    //@SequenceGenerator(name = "testing_service_form_seq", sequenceName = "testing_service_form_sequence", allocationSize = 1)
    //@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "testing_service_form_seq")
    @Column(name = "service_form_id")
    private int serviceFormId;

    @Column(name = "content", nullable = false, length = 255)
    private String content;

    //Relationship with TestingService
    //1:1
    //@OneToOne
    //@JoinColumn(name = "service_id", insertable = false, updatable = false)
    //private TestingService testingService;

}
