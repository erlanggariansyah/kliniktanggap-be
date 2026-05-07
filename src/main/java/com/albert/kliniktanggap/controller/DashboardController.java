package com.albert.kliniktanggap.controller;

import com.albert.kliniktanggap.dto.ApiResponse;
import com.albert.kliniktanggap.dto.response.DashboardAdminResponse;
import com.albert.kliniktanggap.dto.response.DashboardDoctorResponse;
import com.albert.kliniktanggap.dto.response.DashboardFrontdeskResponse;
import com.albert.kliniktanggap.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/frontdesk")
    public ResponseEntity<ApiResponse<DashboardFrontdeskResponse>> frontdesk() {
        return ResponseEntity.ok(ApiResponse.ok(dashboardService.getFrontdeskDashboard()));
    }

    @GetMapping("/doctor")
    public ResponseEntity<ApiResponse<DashboardDoctorResponse>> doctor() {
        return ResponseEntity.ok(ApiResponse.ok(dashboardService.getDoctorDashboard()));
    }

    @GetMapping("/admin")
    public ResponseEntity<ApiResponse<DashboardAdminResponse>> admin() {
        return ResponseEntity.ok(ApiResponse.ok(dashboardService.getAdminDashboard()));
    }
}
