package com.albert.kliniktanggap.controller;

import com.albert.kliniktanggap.dto.ApiResponse;
import com.albert.kliniktanggap.dto.request.PatientRequest;
import com.albert.kliniktanggap.dto.response.PatientResponse;
import com.albert.kliniktanggap.enums.PatientStatus;
import com.albert.kliniktanggap.service.PatientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PatientController {

    private final PatientService patientService;

    @PostMapping
    public ResponseEntity<ApiResponse<PatientResponse>> create(@Valid @RequestBody PatientRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("Pasien berhasil ditambahkan", patientService.create(request)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<PatientResponse>>> findAll() {
        return ResponseEntity.ok(ApiResponse.ok(patientService.findAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PatientResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(patientService.findById(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PatientResponse>> update(@PathVariable Long id, @Valid @RequestBody PatientRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("Pasien berhasil diperbarui", patientService.update(id, request)));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<List<PatientResponse>>> findByStatus(@PathVariable PatientStatus status) {
        return ResponseEntity.ok(ApiResponse.ok(patientService.findByStatus(status)));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<PatientResponse>>> search(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) List<PatientStatus> statuses) {
        return ResponseEntity.ok(ApiResponse.ok(patientService.search(q, statuses)));
    }
}
