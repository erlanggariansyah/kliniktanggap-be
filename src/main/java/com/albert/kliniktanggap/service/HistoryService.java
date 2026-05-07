package com.albert.kliniktanggap.service;

import com.albert.kliniktanggap.dto.response.HistoryResponse;

import java.time.LocalDate;
import java.util.List;

public interface HistoryService {
    List<HistoryResponse> findAll(String search, LocalDate date);
}
