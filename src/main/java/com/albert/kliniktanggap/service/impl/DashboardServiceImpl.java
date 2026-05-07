package com.albert.kliniktanggap.service.impl;

import com.albert.kliniktanggap.dto.response.*;
import com.albert.kliniktanggap.entity.Patient;
import com.albert.kliniktanggap.entity.WeightConfig;
import com.albert.kliniktanggap.enums.PatientStatus;
import com.albert.kliniktanggap.enums.PriorityLevel;
import com.albert.kliniktanggap.repository.ActivityLogRepository;
import com.albert.kliniktanggap.repository.PatientRepository;
import com.albert.kliniktanggap.repository.UserRepository;
import com.albert.kliniktanggap.repository.WeightConfigRepository;
import com.albert.kliniktanggap.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final PatientRepository patientRepository;
    private final UserRepository userRepository;
    private final WeightConfigRepository weightConfigRepository;
    private final ActivityLogRepository activityLogRepository;

    @Override
    public DashboardFrontdeskResponse getFrontdeskDashboard() {
        LocalDateTime todayStart = LocalDateTime.now().toLocalDate().atStartOfDay();
        long totalToday = patientRepository.countByCreatedAtGreaterThanEqual(todayStart);
        long high = patientRepository.countByPriorityAndStatus(PriorityLevel.HIGH, PatientStatus.WAITING);
        long medium = patientRepository.countByPriorityAndStatus(PriorityLevel.MEDIUM, PatientStatus.WAITING);
        long low = patientRepository.countByPriorityAndStatus(PriorityLevel.LOW, PatientStatus.WAITING);

        List<PatientResponse> latest = patientRepository.findAll().stream()
                .sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()))
                .limit(5)
                .map(PatientServiceImpl::toResponse)
                .collect(Collectors.toList());

        return DashboardFrontdeskResponse.builder()
                .totalToday(totalToday)
                .highCount(high)
                .mediumCount(medium)
                .lowCount(low)
                .latestPatients(latest)
                .build();
    }

    @Override
    public DashboardDoctorResponse getDoctorDashboard() {
        long high = patientRepository.countByPriorityAndStatus(PriorityLevel.HIGH, PatientStatus.WAITING);
        long medium = patientRepository.countByPriorityAndStatus(PriorityLevel.MEDIUM, PatientStatus.WAITING);
        long low = patientRepository.countByPriorityAndStatus(PriorityLevel.LOW, PatientStatus.WAITING);
        long total = patientRepository.count();
        long waiting = patientRepository.countByStatus(PatientStatus.WAITING);
        long inProgress = patientRepository.countByStatus(PatientStatus.IN_PROGRESS);
        long completed = patientRepository.countByStatus(PatientStatus.COMPLETED);

        List<Patient> waitingList = patientRepository.findByStatusOrderByPriorityDescScoreDescCreatedAtAsc(PatientStatus.WAITING);
        PatientResponse next = waitingList.isEmpty() ? null : PatientServiceImpl.toResponse(waitingList.get(0));

        List<Patient> inProgressList = patientRepository.findByStatus(PatientStatus.IN_PROGRESS);
        PatientResponse current = inProgressList.isEmpty() ? null : PatientServiceImpl.toResponse(inProgressList.get(0));

        return DashboardDoctorResponse.builder()
                .highCount(high)
                .mediumCount(medium)
                .lowCount(low)
                .total(total)
                .waiting(waiting)
                .inProgress(inProgress)
                .completed(completed)
                .nextPatient(next)
                .currentPatient(current)
                .build();
    }

    @Override
    public DashboardAdminResponse getAdminDashboard() {
        long totalUsers = userRepository.count();
        long totalPatients = patientRepository.count();

        WeightConfig config = weightConfigRepository.findById(1L)
                .orElseGet(() -> weightConfigRepository.save(WeightConfig.builder().build()));

        WeightConfigResponse weightResp = WeightConfigResponse.builder()
                .id(config.getId())
                .severityWeight(config.getSeverityWeight())
                .ageWeight(config.getAgeWeight())
                .durationWeight(config.getDurationWeight())
                .historyWeight(config.getHistoryWeight())
                .total(config.getSeverityWeight() + config.getAgeWeight() + config.getDurationWeight() + config.getHistoryWeight())
                .updatedBy(config.getUpdatedBy())
                .updatedAt(config.getUpdatedAt())
                .build();

        List<ActivityLogResponse> logs = activityLogRepository.findTop10ByOrderByCreatedAtDesc()
                .stream()
                .map(l -> ActivityLogResponse.builder()
                        .id(l.getId())
                        .type(l.getType())
                        .typeLabel(l.getType() != null ? l.getType().getLabel() : "")
                        .description(l.getDescription())
                        .detail(l.getDetail())
                        .userName(l.getUserName())
                        .userRole(l.getUserRole())
                        .createdAt(l.getCreatedAt())
                        .build())
                .collect(Collectors.toList());

        return DashboardAdminResponse.builder()
                .totalUsers(totalUsers)
                .totalPatients(totalPatients)
                .systemStatus("Online")
                .configStatus("Aktif")
                .weightConfig(weightResp)
                .latestActivities(logs)
                .build();
    }
}
