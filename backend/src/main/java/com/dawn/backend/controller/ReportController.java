package com.dawn.backend.controller;

import com.dawn.backend.dto.response.ReportResponseDTO;
import com.dawn.backend.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("report")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/export/{format}")
    public ResponseEntity<byte[]> generateReport(@PathVariable String format) {
        ReportResponseDTO report = reportService.exportReport(format);
        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + report.getFilename())
                .contentType(MediaType.parseMediaType(report.getContentType()))
                .body(report.getData());
    }
}
