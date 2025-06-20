package com.gender_healthcare_system.entities.todo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Nationalized;

import java.io.Serializable;

@Entity
@Data
@ToString(exclude = "testingService")
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "PriceList")
public class PriceList implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //@SequenceGenerator(name = "price_list_seq", sequenceName = "price_list_sequence", allocationSize = 1)
    //@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "price_list_seq")
    @Column(name = "price_id")
    private int priceId;

    //Relationship with TestingService
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id", nullable = false)
    private TestingService testingService;

    @Column(name = "price", nullable = false)
    private long price;

    @Nationalized
    @Column(name = "description", length = 255)
    private String description;

}
