package com.albert.kliniktanggap.service.impl;

import com.albert.kliniktanggap.dto.request.WeightConfigRequest;
import com.albert.kliniktanggap.dto.response.PriorityPreviewResponse;
import com.albert.kliniktanggap.dto.response.WeightConfigResponse;
import com.albert.kliniktanggap.entity.Patient;
import com.albert.kliniktanggap.entity.WeightConfig;
import com.albert.kliniktanggap.enums.ActivityType;
import com.albert.kliniktanggap.enums.PriorityLevel;
import com.albert.kliniktanggap.repository.PatientRepository;
import com.albert.kliniktanggap.repository.WeightConfigRepository;
import com.albert.kliniktanggap.service.ActivityLogService;
import com.albert.kliniktanggap.service.PriorityCalculatorService;
import com.albert.kliniktanggap.service.WeightConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WeightConfigServiceImpl implements WeightConfigService {

    private final WeightConfigRepository weightConfigRepository;
    private final PatientRepository patientRepository;
    private final PriorityCalculatorService calculator;
    private final ActivityLogService activityLogService;

    @Override
    public WeightConfigResponse getCurrent() {
        WeightConfig config = weightConfigRepository.findById(1L)
                .orElseGet(() -> weightConfigRepository.save(WeightConfig.builder().build()));
        return toResponse(config);
    }

    @Override
    @Transactional
    public WeightConfigResponse update(WeightConfigRequest request, String userName) {
        WeightConfig config = weightConfigRepository.findById(1L)
                .orElseGet(() -> WeightConfig.builder().build());

        double oldSev = config.getSeverityWeight();
        double oldAge = config.getAgeWeight();

        config.setSeverityWeight(request.getSeverityWeight());
        config.setAgeWeight(request.getAgeWeight());
        config.setDurationWeight(request.getDurationWeight());
        config.setHistoryWeight(request.getHistoryWeight());
        config.setUpdatedBy(userName);

        WeightConfig saved = weightConfigRepository.save(config);

        // Recalculate all waiting patients
        List<Patient> waiting = patientRepository.findByStatus(
                com.albert.kliniktanggap.enums.PatientStatus.WAITING);
        for (Patient p : waiting) {
            double score = calculator.calculateScore(p, saved);
            p.setScore(score);
            p.setPriority(PriorityCalculatorServiceImpl.determinePriority(score));
        }
        patientRepository.saveAll(waiting);

        activityLogService.log(
                ActivityType.CONFIGURATION,
                "Perubahan bobot prioritas",
                "Keparahan: " + oldSev + " -> " + saved.getSeverityWeight() + ", Usia: " + oldAge + " -> " + saved.getAgeWeight(),
                userName,
                "Admin"
        );

        return toResponse(saved);
    }

    @Override
    public List<PriorityPreviewResponse> preview(WeightConfigRequest request) {
        WeightConfig temp = WeightConfig.builder()
                .severityWeight(request.getSeverityWeight())
                .ageWeight(request.getAgeWeight())
                .durationWeight(request.getDurationWeight())
                .historyWeight(request.getHistoryWeight())
                .build();

        List<Patient> patients = patientRepository.findAll();
        return patients.stream().map(p -> {
            double oldScore = p.getScore() != null ? p.getScore() : 0.0;
            double newScore = calculator.calculateScore(p, temp);
            return PriorityPreviewResponse.builder()
                    .patientId(p.getId())
                    .name(p.getName())
                    .age(p.getAge())
                    .severityLabel(p.getSeverity() != null ? p.getSeverity().getLabel() : "")
                    .oldScore(oldScore)
                    .newScore(newScore)
                    .build();
        }).collect(Collectors.toList());
    }

    private WeightConfigResponse toResponse(WeightConfig c) {
        return WeightConfigResponse.builder()
                .id(c.getId())
                .severityWeight(c.getSeverityWeight())
                .ageWeight(c.getAgeWeight())
                .durationWeight(c.getDurationWeight())
                .historyWeight(c.getHistoryWeight())
                .total(c.getSeverityWeight() + c.getAgeWeight() + c.getDurationWeight() + c.getHistoryWeight())
                .updatedBy(c.getUpdatedBy())
                .updatedAt(c.getUpdatedAt())
                .build();
    }
}
