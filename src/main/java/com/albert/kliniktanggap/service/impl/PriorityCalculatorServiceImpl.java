package com.albert.kliniktanggap.service.impl;

import com.albert.kliniktanggap.entity.Patient;
import com.albert.kliniktanggap.entity.WeightConfig;
import com.albert.kliniktanggap.enums.PriorityLevel;
import com.albert.kliniktanggap.service.PriorityCalculatorService;
import org.springframework.stereotype.Service;

@Service
public class PriorityCalculatorServiceImpl implements PriorityCalculatorService {

    @Override
    public double calculateScore(Patient patient, WeightConfig weights) {
        if (weights == null) {
            weights = WeightConfig.builder()
                    .severityWeight(0.4)
                    .ageWeight(0.2)
                    .durationWeight(0.2)
                    .historyWeight(0.2)
                    .build();
        }

        double severityScore = patient.getSeverity().getBaseScore();
        double durationScore = patient.getDuration().getBaseScore();

        // Age score: normalize to 0-10 scale roughly
        double ageScore = Math.min(patient.getAge() / 6.5, 12.0);

        // History score: each condition adds 2.0
        double historyScore = patient.getMedicalHistories() != null ? patient.getMedicalHistories().size() * 2.0 : 0.0;

        double rawScore = (severityScore * weights.getSeverityWeight())
                        + (durationScore * weights.getDurationWeight())
                        + (ageScore * weights.getAgeWeight())
                        + (historyScore * weights.getHistoryWeight());

        return Math.round(rawScore * 10.0) / 10.0;
    }

    public static PriorityLevel determinePriority(double score) {
        if (score >= 7.5) return PriorityLevel.HIGH;
        if (score >= 3.5) return PriorityLevel.MEDIUM;
        return PriorityLevel.LOW;
    }
}
