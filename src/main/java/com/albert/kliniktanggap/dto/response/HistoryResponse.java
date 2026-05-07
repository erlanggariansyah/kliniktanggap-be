package com.albert.kliniktanggap.dto.response;

import com.albert.kliniktanggap.enums.PriorityLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistoryResponse {
    private Long id;
    private String name;
    private Integer age;
    private String genderLabel;
    private Double score;
    private PriorityLevel priority;
    private String priorityLabel;
    private String complaint;
    private String durationLabel;
    private String severityLabel;
    private LocalDateTime completedAt;
    private String notes;
}
