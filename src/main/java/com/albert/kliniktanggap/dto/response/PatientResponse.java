package com.albert.kliniktanggap.dto.response;

import com.albert.kliniktanggap.enums.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientResponse {
    private Long id;
    private String name;
    private Integer age;
    private Gender gender;
    private String phone;
    private String complaint;
    private Duration duration;
    private Severity severity;
    private List<String> medicalHistories;
    private Double score;
    private PriorityLevel priority;
    private PatientStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime queueEntryTime;
    private LocalDateTime servedTime;
    private LocalDateTime completedAt;
    private String notes;
}
