package com.gender_healthcare_system.entities.todo;

import com.gender_healthcare_system.entities.enu.TestingServiceStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Nationalized;

import java.util.List;

@Entity
@Table(name = "TestingService")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestingService {

    @Id
    @SequenceGenerator(name = "testing_service_seq", sequenceName = "testing_service_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "testing_service_seq")
    @Column(name = "service_id")
    private int serviceId;

    //Relationship with TestingServiceType
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_type_id", nullable = false)
    private TestingServiceType testingServiceType;

    @Nationalized
    @Column(name = "service_name", nullable = false, length = 100)
    private String serviceName;

    @Nationalized
    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private TestingServiceStatus status;

    // Relationship with TestingServiceHistory
    @OneToMany(mappedBy = "testingService", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TestingServiceHistory> testingServiceHistories;

    //One-to-one relationship with TestingServiceForm
    @OneToOne
    @MapsId
    @JoinColumn(name = "service_id", nullable = false)
    private TestingServiceForm testingServiceForm;

}
