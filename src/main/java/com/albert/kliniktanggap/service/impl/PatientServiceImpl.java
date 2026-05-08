package com.albert.kliniktanggap.service.impl;

import com.albert.kliniktanggap.dto.request.PatientRequest;
import com.albert.kliniktanggap.dto.response.PatientResponse;
import com.albert.kliniktanggap.entity.Patient;
import com.albert.kliniktanggap.entity.WeightConfig;
import com.albert.kliniktanggap.enums.PatientStatus;
import com.albert.kliniktanggap.repository.PatientRepository;
import com.albert.kliniktanggap.repository.WeightConfigRepository;
import com.albert.kliniktanggap.dto.request.StatusUpdateRequest;
import com.albert.kliniktanggap.service.ActivityLogService;
import com.albert.kliniktanggap.service.PatientService;
import com.albert.kliniktanggap.service.PriorityCalculatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {
    private final PatientRepository patientRepository;
    private final WeightConfigRepository weightConfigRepository;
    private final PriorityCalculatorService calculator;
    private final ActivityLogService activityLogService;

    @Override
    @Transactional
    public PatientResponse create(PatientRequest request) {
        WeightConfig weights = weightConfigRepository.findById(1L)
                .orElseGet(() -> weightConfigRepository.save(WeightConfig.builder().build()));

        Patient patient = Patient.builder()
                .name(request.getName())
                .age(request.getAge())
                .gender(request.getGender())
                .phone(request.getPhone())
                .complaint(request.getComplaint())
                .duration(request.getDuration())
                .severity(request.getSeverity())
                .medicalHistories(request.getMedicalHistories() != null ? request.getMedicalHistories() : List.of())
                .status(PatientStatus.WAITING)
                .queueEntryTime(LocalDateTime.now())
                .build();

        double score = calculator.calculateScore(patient, weights);
        patient.setScore(score);
        patient.setPriority(PriorityCalculatorServiceImpl.determinePriority(score));

        Patient saved = patientRepository.save(patient);

        activityLogService.log(
                com.albert.kliniktanggap.enums.ActivityType.INPUT,
                "Input pasien baru",
                "Pasien: " + saved.getName() + ", Skor: " + saved.getScore() + ", Prioritas: " + saved.getPriority().getLabel(),
                "Petugas Front Desk",
                "Petugas"
        );

        return toResponse(saved);
    }

    @Override
    public List<PatientResponse> findAll() {
        return patientRepository.findAll().stream()
                .map(this::mapResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<PatientResponse> findByStatus(PatientStatus status) {
        return patientRepository.findByStatus(status).stream()
                .map(this::mapResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<PatientResponse> search(String search, List<PatientStatus> statuses) {
        if (statuses == null || statuses.isEmpty()) {
            statuses = List.of(PatientStatus.WAITING, PatientStatus.IN_PROGRESS);
        }
        if (search == null || search.isBlank()) {
            return patientRepository.findByStatusInOrderByPriorityDescScoreDescCreatedAtAsc(statuses)
                    .stream().map(this::mapResponse).collect(Collectors.toList());
        }
        return patientRepository.searchByStatusesAndName(statuses, search)
                .stream().map(this::mapResponse).collect(Collectors.toList());
    }

    @Override
    public PatientResponse findById(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        return toResponse(patient);
    }

    @Override
    @Transactional
    public PatientResponse update(Long id, PatientRequest request) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        patient.setName(request.getName());
        patient.setAge(request.getAge());
        patient.setGender(request.getGender());
        patient.setPhone(request.getPhone());
        patient.setComplaint(request.getComplaint());
        patient.setDuration(request.getDuration());
        patient.setSeverity(request.getSeverity());
        patient.setMedicalHistories(request.getMedicalHistories() != null ? request.getMedicalHistories() : List.of());

        WeightConfig weights = weightConfigRepository.findById(1L)
                .orElseGet(() -> weightConfigRepository.save(WeightConfig.builder().build()));

        double score = calculator.calculateScore(patient, weights);
        patient.setScore(score);
        patient.setPriority(PriorityCalculatorServiceImpl.determinePriority(score));

        return toResponse(patientRepository.save(patient));
    }

    @Override
    @Transactional
    public PatientResponse updateStatus(Long id, StatusUpdateRequest request) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        patient.setStatus(request.getStatus());
        if (request.getNotes() != null && !request.getNotes().isBlank()) {
            patient.setNotes(request.getNotes());
        }

        if (request.getStatus() == PatientStatus.IN_PROGRESS && patient.getServedTime() == null) {
            patient.setServedTime(LocalDateTime.now());
        }
        if ((request.getStatus() == PatientStatus.COMPLETED || request.getStatus() == PatientStatus.REFERRED) && patient.getCompletedAt() == null) {
            patient.setCompletedAt(LocalDateTime.now());
        }

        Patient saved = patientRepository.save(patient);

        activityLogService.log(
                request.getStatus() == PatientStatus.IN_PROGRESS ? com.albert.kliniktanggap.enums.ActivityType.INPUT :
                        request.getStatus() == PatientStatus.COMPLETED ? com.albert.kliniktanggap.enums.ActivityType.COMPLETED :
                        request.getStatus() == PatientStatus.REFERRED ? com.albert.kliniktanggap.enums.ActivityType.REFERRED :
                        com.albert.kliniktanggap.enums.ActivityType.INPUT,
                request.getStatus() == PatientStatus.IN_PROGRESS ? "Pasien mulai dilayani" :
                        request.getStatus() == PatientStatus.COMPLETED ? "Pasien selesai dilayani" :
                        request.getStatus() == PatientStatus.REFERRED ? "Pasien dirujuk" :
                        "Perubahan status pasien",
                "Pasien: " + saved.getName() + (request.getNotes() != null && !request.getNotes().isBlank() ? ", Catatan: " + request.getNotes() : ""),
                "Sistem",
                "Sistem"
        );

        return toResponse(saved);
    }

    public static PatientResponse toResponse(Patient p) {
        return PatientResponse.builder()
                .id(p.getId())
                .name(p.getName())
                .age(p.getAge())
                .gender(p.getGender())
                .phone(p.getPhone())
                .complaint(p.getComplaint())
                .duration(p.getDuration())
                .severity(p.getSeverity())
                .medicalHistories(p.getMedicalHistories())
                .score(p.getScore())
                .priority(p.getPriority())
                .status(p.getStatus())
                .createdAt(p.getCreatedAt())
                .queueEntryTime(p.getQueueEntryTime())
                .servedTime(p.getServedTime())
                .completedAt(p.getCompletedAt())
                .notes(p.getNotes())
                .build();
    }

    public PatientResponse mapResponse(Patient p) {
        return PatientResponse.builder()
                .id(p.getId())
                .name(p.getName())
                .age(p.getAge())
                .gender(p.getGender())
                .phone(p.getPhone())
                .complaint(p.getComplaint())
                .duration(p.getDuration())
                .severity(p.getSeverity())
                .medicalHistories(p.getMedicalHistories())
                .score(p.getScore())
                .priority(p.getPriority())
                .status(p.getStatus())
                .createdAt(p.getCreatedAt())
                .queueEntryTime(p.getQueueEntryTime())
                .servedTime(p.getServedTime())
                .completedAt(p.getCompletedAt())
                .notes(p.getNotes())
                .build();
    }
}
