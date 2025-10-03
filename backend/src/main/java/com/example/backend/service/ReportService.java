package com.example.backend.service;

import com.example.backend.dto.response.ReportResponseDTO;

public interface ReportService {
    ReportResponseDTO exportReport(String reportFormat);
}
