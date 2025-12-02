package com.dawn.backend.service;

import com.dawn.backend.dto.response.ReportResponse;

public interface ReportService {
    ReportResponse exportReport(String reportFormat);
}
