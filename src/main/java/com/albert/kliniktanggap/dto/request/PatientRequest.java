package com.albert.kliniktanggap.dto.request;

import com.albert.kliniktanggap.enums.Duration;
import com.albert.kliniktanggap.enums.Gender;
import com.albert.kliniktanggap.enums.Severity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class PatientRequest {
    @NotBlank
    private String name;
    @NotNull
    private Integer age;
    @NotNull
    private Gender gender;
    private String phone;
    @NotBlank
    private String complaint;
    @NotNull
    private Duration duration;
    @NotNull
    private Severity severity;
    private List<String> medicalHistories;
}
