package com.albert.kliniktanggap.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardFrontdeskResponse {
    private long totalToday;
    private long highCount;
    private long mediumCount;
    private long lowCount;
    private List<PatientResponse> latestPatients;
}
