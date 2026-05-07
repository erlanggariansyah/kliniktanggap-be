package com.albert.kliniktanggap.dto.response;

import com.albert.kliniktanggap.enums.ActivityType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActivityLogResponse {
    private Long id;
    private ActivityType type;
    private String typeLabel;
    private String description;
    private String detail;
    private String userName;
    private String userRole;
    private LocalDateTime createdAt;
}
