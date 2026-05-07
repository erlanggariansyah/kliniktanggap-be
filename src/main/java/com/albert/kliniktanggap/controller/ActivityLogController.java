package com.albert.kliniktanggap.controller;

import com.albert.kliniktanggap.dto.ApiResponse;
import com.albert.kliniktanggap.dto.response.ActivityLogResponse;
import com.albert.kliniktanggap.enums.ActivityType;
import com.albert.kliniktanggap.service.ActivityLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/logs")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ActivityLogController {

    private final ActivityLogService activityLogService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ActivityLogResponse>>> findAll(
            @RequestParam(required = false) ActivityType type,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) LocalDate date) {
        return ResponseEntity.ok(ApiResponse.ok(activityLogService.findAll(type, search, date)));
    }

    @GetMapping("/latest")
    public ResponseEntity<ApiResponse<List<ActivityLogResponse>>> latest() {
        return ResponseEntity.ok(ApiResponse.ok(activityLogService.findLatest(10)));
    }
}
