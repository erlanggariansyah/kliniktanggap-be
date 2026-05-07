package com.albert.kliniktanggap.controller;

import com.albert.kliniktanggap.dto.ApiResponse;
import com.albert.kliniktanggap.dto.response.HistoryResponse;
import com.albert.kliniktanggap.service.HistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/history")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class HistoryController {

    private final HistoryService historyService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<HistoryResponse>>> findAll(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) LocalDate date) {
        return ResponseEntity.ok(ApiResponse.ok(historyService.findAll(search, date)));
    }
}
