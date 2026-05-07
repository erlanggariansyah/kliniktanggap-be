package com.albert.kliniktanggap.config;

import com.albert.kliniktanggap.entity.*;
import com.albert.kliniktanggap.enums.*;
import com.albert.kliniktanggap.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    private final WeightConfigRepository weightConfigRepository;
    private final ActivityLogRepository activityLogRepository;

    @Override
    @Transactional
    public void run(String... args) {
        initUsers();
        initWeightConfig();
        initPatients();
        initLogs();
    }

    private void initUsers() {
        if (userRepository.count() > 0) return;
        userRepository.save(User.builder().name("Petugas Front Desk").email("petugas@klinik.com").password("password").role(UserRole.PETUGAS).active(true).lastLoginAt(LocalDateTime.of(2024, 1, 15, 8, 0)).build());
        userRepository.save(User.builder().name("Dr. Ahmad").email("dokter@klinik.com").password("password").role(UserRole.DOKTER).active(true).lastLoginAt(LocalDateTime.of(2024, 1, 15, 9, 0)).build());
        userRepository.save(User.builder().name("Admin Sistem").email("admin@klinik.com").password("password").role(UserRole.ADMIN).active(true).lastLoginAt(LocalDateTime.of(2024, 1, 15, 7, 0)).build());
    }

    private void initWeightConfig() {
        if (weightConfigRepository.count() > 0) return;
        weightConfigRepository.save(WeightConfig.builder()
                .severityWeight(0.4)
                .ageWeight(0.2)
                .durationWeight(0.2)
                .historyWeight(0.2)
                .updatedBy("Admin")
                .build());
    }

    private void initPatients() {
        if (patientRepository.count() > 0) return;

        patientRepository.save(Patient.builder()
                .name("Siti Aminah").age(67).gender(Gender.FEMALE).phone("081234567890")
                .complaint("Sesak napas dan nyeri dada")
                .duration(Duration.ONE_TO_THREE_DAYS).severity(Severity.SEVERE)
                .medicalHistories(List.of("Hipertensi", "Jantung"))
                .score(9.2).priority(PriorityLevel.HIGH)
                .status(PatientStatus.WAITING)
                .queueEntryTime(LocalDateTime.of(2024, 1, 15, 9, 15))
                .build());

        patientRepository.save(Patient.builder()
                .name("Ahmad Surya").age(35).gender(Gender.MALE).phone("082345678901")
                .complaint("Demam tinggi dan batuk berdahak")
                .duration(Duration.THREE_TO_SEVEN_DAYS).severity(Severity.MODERATE)
                .medicalHistories(List.of())
                .score(7.5).priority(PriorityLevel.MEDIUM)
                .status(PatientStatus.WAITING)
                .queueEntryTime(LocalDateTime.of(2024, 1, 15, 8, 30))
                .build());

        patientRepository.save(Patient.builder()
                .name("Budi Santoso").age(25).gender(Gender.MALE).phone("083456789012")
                .complaint("Sakit kepala ringan")
                .duration(Duration.LESS_THAN_1_DAY).severity(Severity.MILD)
                .medicalHistories(List.of())
                .score(2.1).priority(PriorityLevel.LOW)
                .status(PatientStatus.COMPLETED)
                .queueEntryTime(LocalDateTime.of(2024, 1, 15, 7, 45))
                .completedAt(LocalDateTime.of(2024, 1, 15, 7, 45))
                .notes("Diberikan obat pereda nyeri")
                .build());
    }

    private void initLogs() {
        if (activityLogRepository.count() > 0) return;
        activityLogRepository.save(ActivityLog.builder().type(ActivityType.INPUT).description("Input pasien baru").detail("Pasien: Siti Aminah, Skor: 9.2, Prioritas: Tinggi").userName("Petugas Front Desk").userRole("Petugas").createdAt(LocalDateTime.of(2024, 1, 15, 10, 10)).build());
        activityLogRepository.save(ActivityLog.builder().type(ActivityType.REFERRED).description("Pasien dirujuk").detail("Pasien: Siti Aminah, Rujukan: RS Harapan Kita").userName("Dr. Ahmad").userRole("Dokter").createdAt(LocalDateTime.of(2024, 1, 15, 9, 45)).build());
        activityLogRepository.save(ActivityLog.builder().type(ActivityType.LOGIN).description("Login user").detail("Login berhasil dari browser Chrome").userName("Dr. Ahmad").userRole("Dokter").createdAt(LocalDateTime.of(2024, 1, 15, 10, 15)).build());
        activityLogRepository.save(ActivityLog.builder().type(ActivityType.COMPLETED).description("Pasien selesai dilayani").detail("Pasien: Budi Santoso, Diagnosis: Sakit kepala ringan").userName("Dr. Ahmad").userRole("Dokter").createdAt(LocalDateTime.of(2024, 1, 15, 10, 20)).build());
        activityLogRepository.save(ActivityLog.builder().type(ActivityType.CONFIGURATION).description("Perubahan bobot prioritas").detail("Keparahan: 0.4 -> 0.5, Usia: 0.2 -> 0.1").userName("Admin Sistem").userRole("Admin").createdAt(LocalDateTime.of(2024, 1, 15, 10, 25)).build());
        activityLogRepository.save(ActivityLog.builder().type(ActivityType.INPUT).description("Input pasien baru").detail("Pasien: Ahmad Surya, Skor: 7.5, Prioritas: Sedang").userName("Petugas Front Desk").userRole("Petugas").createdAt(LocalDateTime.of(2024, 1, 15, 10, 30)).build());
    }
}
