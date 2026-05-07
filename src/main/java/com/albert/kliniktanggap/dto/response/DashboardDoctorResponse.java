package com.albert.kliniktanggap.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardDoctorResponse {
    private long highCount;
    private long mediumCount;
    private long lowCount;
    private long total;
    private long waiting;
    private long inProgress;
    private long completed;
    private PatientResponse nextPatient;
    private PatientResponse currentPatient;
}
