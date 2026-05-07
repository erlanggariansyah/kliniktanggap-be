package com.albert.kliniktanggap.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class WeightConfigRequest {
    @NotNull
    private Double severityWeight;
    @NotNull
    private Double ageWeight;
    @NotNull
    private Double durationWeight;
    @NotNull
    private Double historyWeight;
}
