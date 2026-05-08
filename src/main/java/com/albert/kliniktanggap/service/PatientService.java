package com.albert.kliniktanggap.service;

import com.albert.kliniktanggap.dto.request.PatientRequest;
import com.albert.kliniktanggap.dto.response.PatientResponse;
import com.albert.kliniktanggap.enums.PatientStatus;

import java.util.List;

public interface PatientService {
    PatientResponse create(PatientRequest request);
    List<PatientResponse> findAll();
    List<PatientResponse> findByStatus(PatientStatus status);
    List<PatientResponse> search(String search, List<PatientStatus> statuses);
    PatientResponse findById(Long id);
    PatientResponse update(Long id, PatientRequest request);
    PatientResponse updateStatus(Long id, com.albert.kliniktanggap.dto.request.StatusUpdateRequest request);
}
