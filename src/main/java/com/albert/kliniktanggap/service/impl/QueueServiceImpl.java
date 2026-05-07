package com.albert.kliniktanggap.service.impl;

import com.albert.kliniktanggap.dto.request.QueueActionRequest;
import com.albert.kliniktanggap.dto.response.PatientResponse;
import com.albert.kliniktanggap.dto.response.QueueResponse;
import com.albert.kliniktanggap.entity.Patient;
import com.albert.kliniktanggap.enums.ActivityType;
import com.albert.kliniktanggap.enums.PatientStatus;
import com.albert.kliniktanggap.enums.PriorityLevel;
import com.albert.kliniktanggap.repository.PatientRepository;
import com.albert.kliniktanggap.service.ActivityLogService;
import com.albert.kliniktanggap.service.QueueService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QueueServiceImpl implements QueueService {

    private final PatientRepository patientRepository;
    private final ActivityLogService activityLogService;

    @Override
    @Transactional
    public PatientResponse enqueue(Long patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        patient.setStatus(PatientStatus.WAITING);
        patient.setQueueEntryTime(LocalDateTime.now());
        return PatientServiceImpl.toResponse(patientRepository.save(patient));
    }

    @Override
    public List<QueueResponse> getActiveQueue() {
        return patientRepository.findByStatusOrderByPriorityDescScoreDescCreatedAtAsc(PatientStatus.WAITING)
                .stream()
                .map(this::toQueueResponse)
                .collect(Collectors.toList());
    }

    @Override
    public PatientResponse getNextPatient() {
        List<Patient> waiting = patientRepository.findByStatusOrderByPriorityDescScoreDescCreatedAtAsc(PatientStatus.WAITING);
        if (waiting.isEmpty()) return null;
        return PatientServiceImpl.toResponse(waiting.get(0));
    }

    @Override
    @Transactional
    public PatientResponse callPatient(Long patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        patient.setStatus(PatientStatus.IN_PROGRESS);
        patient.setServedTime(LocalDateTime.now());
        Patient saved = patientRepository.save(patient);

        activityLogService.log(
                ActivityType.INPUT,
                "Panggil pasien",
                "Pasien: " + saved.getName() + " dipanggil ke ruangan",
                "Dr. Ahmad",
                "Dokter"
        );

        return PatientServiceImpl.toResponse(saved);
    }

    @Override
    @Transactional
    public PatientResponse completePatient(Long patientId, QueueActionRequest request) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        patient.setStatus(PatientStatus.COMPLETED);
        patient.setCompletedAt(LocalDateTime.now());
        patient.setNotes(request.getNotes());
        Patient saved = patientRepository.save(patient);

        activityLogService.log(
                ActivityType.COMPLETED,
                "Pasien selesai dilayani",
                "Pasien: " + saved.getName() + ", Diagnosis: " + saved.getComplaint(),
                "Dr. Ahmad",
                "Dokter"
        );

        return PatientServiceImpl.toResponse(saved);
    }

    @Override
    @Transactional
    public PatientResponse referPatient(Long patientId, QueueActionRequest request) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        patient.setStatus(PatientStatus.REFERRED);
        patient.setCompletedAt(LocalDateTime.now());
        patient.setNotes(request.getNotes());
        Patient saved = patientRepository.save(patient);

        activityLogService.log(
                ActivityType.REFERRED,
                "Pasien dirujuk",
                "Pasien: " + saved.getName() + ", Rujukan: " + (request.getNotes() != null ? request.getNotes() : "RS Harapan Kita"),
                "Dr. Ahmad",
                "Dokter"
        );

        return PatientServiceImpl.toResponse(saved);
    }

    private QueueResponse toQueueResponse(Patient p) {
        String waitingTime = "";
        if (p.getQueueEntryTime() != null) {
            Duration d = Duration.between(p.getQueueEntryTime(), LocalDateTime.now());
            long mins = d.toMinutes();
            waitingTime = mins + "m";
        }
        return QueueResponse.builder()
                .id(p.getId())
                .name(p.getName())
                .age(p.getAge())
                .genderLabel(p.getGender() != null ? p.getGender().getLabel() : "")
                .score(p.getScore())
                .priority(p.getPriority())
                .priorityLabel(p.getPriority() != null ? p.getPriority().getLabel() : "")
                .status(p.getStatus())
                .statusLabel(p.getStatus() != null ? p.getStatus().getLabel() : "")
                .queueEntryTime(p.getQueueEntryTime())
                .complaint(p.getComplaint())
                .durationLabel(p.getDuration() != null ? p.getDuration().getLabel() : "")
                .severityLabel(p.getSeverity() != null ? p.getSeverity().getLabel() : "")
                .waitingTime(waitingTime)
                .build();
    }
}
