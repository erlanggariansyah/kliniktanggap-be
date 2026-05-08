package com.albert.kliniktanggap.dto.request;

import com.albert.kliniktanggap.enums.PatientStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StatusUpdateRequest {
    @NotNull
    private PatientStatus status;
    private String notes;
}
