package com.dawn.backend.service;

import com.dawn.backend.dto.response.ReportResponseDTO;

public interface ReportService {
    ReportResponseDTO exportReport(String reportFormat);
}
