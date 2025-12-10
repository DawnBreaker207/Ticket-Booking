package com.dawn.report.service;


import com.dawn.report.dto.response.ReportResponse;

public interface ReportService {
    ReportResponse exportReport(String reportFormat);
}
