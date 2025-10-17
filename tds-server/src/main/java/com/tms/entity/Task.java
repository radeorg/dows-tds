package com.tms.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Lob
    private String description;

    @ManyToOne
    @JoinColumn(name = "responsible_person_id")
    private User responsiblePerson;

    private Integer estimatedTime; // 预计工时（天）

    @Column(nullable = false)
    private String status; // NOT_STARTED, IN_PROGRESS, REVIEWING, COMPLETED

    private String priority; // LOW, MEDIUM, HIGH, URGENT

    private BigDecimal reward; // 佣金

    private String paymentStatus; // NOT_PAID, PARTIALLY_PAID, PAID

    private LocalDateTime deadline;

    @ManyToOne
    @JoinColumn(name = "creator_id", nullable = false)
    private User creator;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt;
}