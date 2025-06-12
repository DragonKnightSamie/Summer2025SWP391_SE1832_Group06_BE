package com.gender_healthcare_system.entities.todo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Nationalized;

@Entity
@Table(name = "TestingServiceResult")
@Data
@ToString(exclude = "testingServiceType")
@NoArgsConstructor
@AllArgsConstructor
public class TestingServiceResult {

    @Id
    @SequenceGenerator(name = "testing_service_result_sequence",
            sequenceName = "testing_service_result_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "testing_service_result_sequence")
    @Column(name = "service_result_id")
    private int serviceResultId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_type_id", nullable = false)
    private TestingServiceType testingServiceType;

    @Nationalized
    @Column(name = "content", nullable = false, length = 50)
    private String title;

    @Nationalized
    @Column(name = "description", length = 100, nullable = false)
    private String description;

}
