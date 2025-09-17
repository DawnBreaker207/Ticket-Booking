package com.example.backend.service;

import com.example.backend.dto.response.ReportDTO;

public interface ReportService {
    ReportDTO exportReport(String reportFormat);
}
