package com.albert.kliniktanggap.service;

import com.albert.kliniktanggap.dto.request.WeightConfigRequest;
import com.albert.kliniktanggap.dto.response.PriorityPreviewResponse;
import com.albert.kliniktanggap.dto.response.WeightConfigResponse;

import java.util.List;

public interface WeightConfigService {
    WeightConfigResponse getCurrent();
    WeightConfigResponse update(WeightConfigRequest request, String userName);
    List<PriorityPreviewResponse> preview(WeightConfigRequest request);
}
