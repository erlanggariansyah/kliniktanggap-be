package com.albert.kliniktanggap.service;

import com.albert.kliniktanggap.dto.response.DashboardAdminResponse;
import com.albert.kliniktanggap.dto.response.DashboardDoctorResponse;
import com.albert.kliniktanggap.dto.response.DashboardFrontdeskResponse;

public interface DashboardService {
    DashboardFrontdeskResponse getFrontdeskDashboard();
    DashboardDoctorResponse getDoctorDashboard();
    DashboardAdminResponse getAdminDashboard();
}
