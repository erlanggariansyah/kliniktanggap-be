package com.albert.kliniktanggap.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WeightConfigResponse {
    private Long id;
    private Double severityWeight;
    private Double ageWeight;
    private Double durationWeight;
    private Double historyWeight;
    private Double total;
    private String updatedBy;
    private LocalDateTime updatedAt;
}
