package com.albert.kliniktanggap.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "weight_configs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WeightConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Builder.Default
    private Double severityWeight = 0.4;

    @Builder.Default
    private Double ageWeight = 0.2;

    @Builder.Default
    private Double durationWeight = 0.2;

    @Builder.Default
    private Double historyWeight = 0.2;

    private String updatedBy;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
