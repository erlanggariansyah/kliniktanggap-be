package com.albert.kliniktanggap.service;

import com.albert.kliniktanggap.dto.response.ActivityLogResponse;
import com.albert.kliniktanggap.enums.ActivityType;

import java.time.LocalDate;
import java.util.List;

public interface ActivityLogService {
    List<ActivityLogResponse> findAll(ActivityType type, String search, LocalDate date);
    List<ActivityLogResponse> findLatest(int limit);
    void log(ActivityType type, String description, String detail, String userName, String userRole);
}
