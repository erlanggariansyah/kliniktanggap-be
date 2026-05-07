package com.albert.kliniktanggap.entity;

import com.albert.kliniktanggap.enums.*;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "patients")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer age;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    private String phone;

    @Column(length = 1000)
    private String complaint;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Duration duration;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Severity severity;

    @ElementCollection
    @CollectionTable(name = "patient_medical_histories", joinColumns = @JoinColumn(name = "patient_id"))
    @Column(name = "history")
    @Builder.Default
    private List<String> medicalHistories = new ArrayList<>();

    private Double score;

    @Enumerated(EnumType.STRING)
    private PriorityLevel priority;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private PatientStatus status = PatientStatus.WAITING;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private LocalDateTime queueEntryTime;
    private LocalDateTime servedTime;
    private LocalDateTime completedAt;

    @Column(length = 1000)
    private String notes;
}
