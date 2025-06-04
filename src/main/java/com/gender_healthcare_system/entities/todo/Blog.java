package com.gender_healthcare_system.entities.todo;

import com.gender_healthcare_system.entities.enu.BlogStatus;
import com.gender_healthcare_system.entities.user.Manager;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "Blog")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Blog {

    @Id
    @SequenceGenerator(name = "blog_sequence", sequenceName = "blog_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "blog_sequence")
    @Column(name = "blog_id")
    private int blogId;

    //Relationship with Manager
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id", nullable = false)
    private Manager manager;

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Column(name = "content", nullable = false, length = 1000)
    private String content;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private BlogStatus status;

}
