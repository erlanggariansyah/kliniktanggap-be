package com.albert.kliniktanggap.service;

import com.albert.kliniktanggap.entity.Patient;
import com.albert.kliniktanggap.entity.WeightConfig;

public interface PriorityCalculatorService {
    double calculateScore(Patient patient, WeightConfig weights);
    default double calculateScore(Patient patient) {
        return calculateScore(patient, null);
    }
}
