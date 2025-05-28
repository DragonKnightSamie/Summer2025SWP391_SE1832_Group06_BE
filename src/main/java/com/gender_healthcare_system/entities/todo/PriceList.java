package com.gender_healthcare_system.entities.todo;

import jakarta.persistence.*;
import org.hibernate.annotations.Nationalized;

@Entity
@Table(name = "PriceList")
public class PriceList {

    @Id
    @SequenceGenerator(name = "price_list_seq", sequenceName = "price_list_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "price_list_seq")
    @Column(name = "price_id")
    private int priceId;

    @Column(name = "price", nullable = false)
    private double price;

    @Nationalized
    @Column(name = "description", length = 255)
    private String description;

    //Relationship with TestingService
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id", nullable = false)
    private TestingService testingService;
}
