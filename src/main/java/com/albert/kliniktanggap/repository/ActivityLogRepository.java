package com.albert.kliniktanggap.repository;

import com.albert.kliniktanggap.entity.ActivityLog;
import com.albert.kliniktanggap.enums.ActivityType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ActivityLogRepository extends JpaRepository<ActivityLog, Long> {
    List<ActivityLog> findTop10ByOrderByCreatedAtDesc();
    List<ActivityLog> findByTypeOrderByCreatedAtDesc(ActivityType type);

    @Query("SELECT a FROM ActivityLog a WHERE (:type IS NULL OR a.type = :type) AND (:search IS NULL OR LOWER(a.description) LIKE LOWER(CONCAT('%', :search, '%'))) AND (:start IS NULL OR a.createdAt >= :start) AND (:end IS NULL OR a.createdAt <= :end) ORDER BY a.createdAt DESC")
    List<ActivityLog> searchLogs(@Param("type") ActivityType type, @Param("search") String search, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    long countByType(ActivityType type);
}
