package com.gender_healthcare_system.entities.todo;

import com.gender_healthcare_system.entities.enu.GenderType;
import com.gender_healthcare_system.entities.enu.MeasureUnit;
import com.gender_healthcare_system.entities.enu.ResultType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Nationalized;

import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "TestingServiceResult")
@Data
@ToString(exclude = "testingServiceType")
@NoArgsConstructor
@AllArgsConstructor
public class TestingServiceResult implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //@SequenceGenerator(name = "testing_service_result_sequence",
    //        sequenceName = "testing_service_result_sequence", allocationSize = 1)
    //@GeneratedValue(strategy = GenerationType.SEQUENCE,
    //        generator = "testing_service_result_sequence")
    @Column(name = "service_result_id")
    private int serviceResultId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_type_id", nullable = false)
    private TestingServiceType testingServiceType;

    @Nationalized
    @Column(name = "title", nullable = false, length = 50)
    private String title;

    @Nationalized
    @Column(name = "description", length = 100, nullable = false)
    private String description;

    @Column(name = "type", nullable = false, length = 15)
    @Enumerated(EnumType.STRING)
    private ResultType type;

    @Column(name = "gender_type", nullable = false, length = 15)
    @Enumerated(EnumType.STRING)
    private GenderType genderType;

    @Nationalized
    @Column(name = "measure_unit", length = 50)
    @Enumerated(EnumType.STRING)
    private MeasureUnit measureUnit;

    @Column(name = "min_value", precision = 3, scale = 3)
    private BigDecimal minValue;

    @Column(name = "max_value", precision = 3, scale = 3)
    private BigDecimal maxValue;

}
