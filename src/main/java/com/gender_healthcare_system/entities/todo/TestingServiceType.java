package com.gender_healthcare_system.entities.todo;

import com.gender_healthcare_system.entities.user.Manager;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Nationalized;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "TestingServiceType")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestingServiceType {

    @Id
    @SequenceGenerator(name = "testing_service_type_seq", sequenceName = "testing_service_type_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "testing_service_type_seq")
    @Column(name = "service_type_id")
    private int serviceTypeId;

    @Nationalized
    @Column(name = "service_type_name", nullable = false, length = 100)
    private String serviceTypeName;

    @Nationalized
    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Nationalized
    @Column(name = "content", length = 255)
    private String content;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    //Relationship with TestingService
    @OneToMany(mappedBy = "testingServiceType", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TestingService> testingServices;


}
