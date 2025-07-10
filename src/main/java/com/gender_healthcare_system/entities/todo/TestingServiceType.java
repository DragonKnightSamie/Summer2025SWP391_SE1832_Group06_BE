package com.gender_healthcare_system.entities.todo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Nationalized;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "TestingServiceType")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestingServiceType implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "service_type_id")
    private int serviceTypeId;

    @Nationalized
    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Nationalized
    @Column(name = "content", length = 255)
    private String content;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    //Relationship with TestingService.
    @OneToMany(mappedBy = "testingServiceType", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TestingService> testingServices;

    //Relationship with TestingServiceResult
    @OneToMany(mappedBy = "testingServiceType", cascade = CascadeType.ALL, orphanRemoval = true)
    //@MapsId // ✅ Map serviceTypeId = serviceResultId
    private List<TestingServiceResult> testingServiceResultList;

    public TestingServiceType(int serviceTypeId, String title,
                              String content, LocalDateTime createdAt) {
        this.serviceTypeId = serviceTypeId;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
    }

    public void addResult(TestingServiceResult result){
        result.setTestingServiceType(this);

        if(this.testingServiceResultList == null){
            this.testingServiceResultList = new ArrayList<>();
        }

        this.testingServiceResultList.add(result);

    }
}
