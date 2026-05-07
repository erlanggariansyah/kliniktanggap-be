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
public class DashboardAdminResponse {
    private long totalUsers;
    private long totalPatients;
    private String systemStatus;
    private String configStatus;
    private WeightConfigResponse weightConfig;
    private List<ActivityLogResponse> latestActivities;
}
