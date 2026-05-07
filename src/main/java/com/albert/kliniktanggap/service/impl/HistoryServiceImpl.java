package com.albert.kliniktanggap.service.impl;

import com.albert.kliniktanggap.dto.response.HistoryResponse;
import com.albert.kliniktanggap.entity.Patient;
import com.albert.kliniktanggap.enums.PatientStatus;
import com.albert.kliniktanggap.repository.PatientRepository;
import com.albert.kliniktanggap.service.HistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HistoryServiceImpl implements HistoryService {

    private final PatientRepository patientRepository;

    @Override
    public List<HistoryResponse> findAll(String search, LocalDate date) {
        List<PatientStatus> statuses = List.of(PatientStatus.COMPLETED, PatientStatus.REFERRED);
        List<Patient> patients;

        if (date != null) {
            LocalDateTime start = date.atStartOfDay();
            LocalDateTime end = date.plusDays(1).atStartOfDay();
            // Simplified: fetch all and filter
            patients = patientRepository.findByStatusInOrderByPriorityDescScoreDescCreatedAtAsc(statuses)
                    .stream()
                    .filter(p -> p.getCompletedAt() != null && !p.getCompletedAt().isBefore(start) && p.getCompletedAt().isBefore(end))
                    .collect(Collectors.toList());
        } else {
            patients = patientRepository.findByStatusInOrderByPriorityDescScoreDescCreatedAtAsc(statuses);
        }

        if (search != null && !search.isBlank()) {
            String s = search.toLowerCase();
            patients = patients.stream()
                    .filter(p -> p.getName().toLowerCase().contains(s) || (p.getComplaint() != null && p.getComplaint().toLowerCase().contains(s)))
                    .collect(Collectors.toList());
        }

        return patients.stream().map(this::toResponse).collect(Collectors.toList());
    }

    private HistoryResponse toResponse(Patient p) {
        return HistoryResponse.builder()
                .id(p.getId())
                .name(p.getName())
                .age(p.getAge())
                .genderLabel(p.getGender() != null ? p.getGender().getLabel() : "")
                .score(p.getScore())
                .priority(p.getPriority())
                .priorityLabel(p.getPriority() != null ? p.getPriority().getLabel() : "")
                .complaint(p.getComplaint())
                .durationLabel(p.getDuration() != null ? p.getDuration().getLabel() : "")
                .severityLabel(p.getSeverity() != null ? p.getSeverity().getLabel() : "")
                .completedAt(p.getCompletedAt())
                .notes(p.getNotes())
                .build();
    }
}
