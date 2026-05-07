package com.albert.kliniktanggap.service.impl;

import com.albert.kliniktanggap.dto.response.ActivityLogResponse;
import com.albert.kliniktanggap.entity.ActivityLog;
import com.albert.kliniktanggap.enums.ActivityType;
import com.albert.kliniktanggap.repository.ActivityLogRepository;
import com.albert.kliniktanggap.service.ActivityLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ActivityLogServiceImpl implements ActivityLogService {

    private final ActivityLogRepository activityLogRepository;

    @Override
    public List<ActivityLogResponse> findAll(ActivityType type, String search, LocalDate date) {
        LocalDateTime start = date != null ? date.atStartOfDay() : null;
        LocalDateTime end = date != null ? date.plusDays(1).atStartOfDay() : null;
        return activityLogRepository.searchLogs(type, search, start, end)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ActivityLogResponse> findLatest(int limit) {
        return activityLogRepository.findTop10ByOrderByCreatedAtDesc()
                .stream()
                .limit(limit)
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void log(ActivityType type, String description, String detail, String userName, String userRole) {
        ActivityLog log = ActivityLog.builder()
                .type(type)
                .description(description)
                .detail(detail)
                .userName(userName)
                .userRole(userRole)
                .build();
        activityLogRepository.save(log);
    }

    private ActivityLogResponse toResponse(ActivityLog l) {
        return ActivityLogResponse.builder()
                .id(l.getId())
                .type(l.getType())
                .typeLabel(l.getType() != null ? l.getType().getLabel() : "")
                .description(l.getDescription())
                .detail(l.getDetail())
                .userName(l.getUserName())
                .userRole(l.getUserRole())
                .createdAt(l.getCreatedAt())
                .build();
    }
}
