package com.albert.kliniktanggap.controller;

import com.albert.kliniktanggap.dto.ApiResponse;
import com.albert.kliniktanggap.dto.request.QueueActionRequest;
import com.albert.kliniktanggap.dto.response.PatientResponse;
import com.albert.kliniktanggap.dto.response.QueueResponse;
import com.albert.kliniktanggap.service.QueueService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/queue")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class QueueController {

    private final QueueService queueService;

    @PostMapping("/{patientId}/enqueue")
    public ResponseEntity<ApiResponse<PatientResponse>> enqueue(@PathVariable Long patientId) {
        return ResponseEntity.ok(ApiResponse.ok("Pasien dimasukkan ke antrian", queueService.enqueue(patientId)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<QueueResponse>>> getActiveQueue() {
        return ResponseEntity.ok(ApiResponse.ok(queueService.getActiveQueue()));
    }

    @GetMapping("/next")
    public ResponseEntity<ApiResponse<PatientResponse>> getNextPatient() {
        PatientResponse next = queueService.getNextPatient();
        if (next == null) {
            return ResponseEntity.ok(ApiResponse.ok("Tidak ada pasien menunggu", null));
        }
        return ResponseEntity.ok(ApiResponse.ok(next));
    }

    @PostMapping("/{patientId}/call")
    public ResponseEntity<ApiResponse<PatientResponse>> callPatient(@PathVariable Long patientId) {
        return ResponseEntity.ok(ApiResponse.ok("Pasien dipanggil", queueService.callPatient(patientId)));
    }

    @PostMapping("/{patientId}/complete")
    public ResponseEntity<ApiResponse<PatientResponse>> complete(@PathVariable Long patientId, @RequestBody QueueActionRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("Pasien selesai dilayani", queueService.completePatient(patientId, request)));
    }

    @PostMapping("/{patientId}/refer")
    public ResponseEntity<ApiResponse<PatientResponse>> refer(@PathVariable Long patientId, @RequestBody QueueActionRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("Pasien dirujuk", queueService.referPatient(patientId, request)));
    }
}
