package com.albert.kliniktanggap.repository;

import com.albert.kliniktanggap.entity.Patient;
import com.albert.kliniktanggap.enums.PatientStatus;
import com.albert.kliniktanggap.enums.PriorityLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    List<Patient> findByStatusOrderByPriorityDescScoreDescCreatedAtAsc(PatientStatus status);
    List<Patient> findByStatusInOrderByPriorityDescScoreDescCreatedAtAsc(List<PatientStatus> statuses);
    List<Patient> findByStatus(PatientStatus status);
    long countByStatus(PatientStatus status);
    long countByPriorityAndStatus(PriorityLevel priority, PatientStatus status);

    @Query("SELECT p FROM Patient p WHERE p.status IN :statuses AND (LOWER(p.name) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(p.complaint) LIKE LOWER(CONCAT('%', :search, '%'))) ORDER BY p.priority DESC, p.score DESC, p.createdAt ASC")
    List<Patient> searchByStatusesAndName(@Param("statuses") List<PatientStatus> statuses, @Param("search") String search);

    @Query("SELECT p FROM Patient p WHERE p.status = :status AND p.createdAt >= :start AND p.createdAt <= :end")
    List<Patient> findByStatusAndDateRange(@Param("status") PatientStatus status, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    long countByCreatedAtGreaterThanEqual(LocalDateTime start);
}
