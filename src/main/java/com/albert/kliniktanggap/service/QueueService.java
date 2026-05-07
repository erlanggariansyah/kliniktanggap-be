package com.albert.kliniktanggap.service;

import com.albert.kliniktanggap.dto.request.QueueActionRequest;
import com.albert.kliniktanggap.dto.response.PatientResponse;
import com.albert.kliniktanggap.dto.response.QueueResponse;

import java.util.List;

public interface QueueService {
    PatientResponse enqueue(Long patientId);
    List<QueueResponse> getActiveQueue();
    PatientResponse getNextPatient();
    PatientResponse callPatient(Long patientId);
    PatientResponse completePatient(Long patientId, QueueActionRequest request);
    PatientResponse referPatient(Long patientId, QueueActionRequest request);
}
