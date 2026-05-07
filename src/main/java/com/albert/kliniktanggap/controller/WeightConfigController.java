package com.albert.kliniktanggap.controller;

import com.albert.kliniktanggap.dto.ApiResponse;
import com.albert.kliniktanggap.dto.request.WeightConfigRequest;
import com.albert.kliniktanggap.dto.response.PriorityPreviewResponse;
import com.albert.kliniktanggap.dto.response.WeightConfigResponse;
import com.albert.kliniktanggap.service.WeightConfigService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/weights")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class WeightConfigController {

    private final WeightConfigService weightConfigService;

    @GetMapping
    public ResponseEntity<ApiResponse<WeightConfigResponse>> getCurrent() {
        return ResponseEntity.ok(ApiResponse.ok(weightConfigService.getCurrent()));
    }

    @PutMapping
    public ResponseEntity<ApiResponse<WeightConfigResponse>> update(@Valid @RequestBody WeightConfigRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("Konfigurasi berhasil disimpan", weightConfigService.update(request, "Admin")));
    }

    @PostMapping("/preview")
    public ResponseEntity<ApiResponse<List<PriorityPreviewResponse>>> preview(@Valid @RequestBody WeightConfigRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(weightConfigService.preview(request)));
    }
}
