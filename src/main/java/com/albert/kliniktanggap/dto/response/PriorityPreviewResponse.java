package com.albert.kliniktanggap.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PriorityPreviewResponse {
    private Long patientId;
    private String name;
    private Integer age;
    private String severityLabel;
    private Double oldScore;
    private Double newScore;
}
