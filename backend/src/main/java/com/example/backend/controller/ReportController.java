package com.example.backend.controller;

import com.example.backend.dto.response.ReportDTO;
import com.example.backend.service.Impl.ReportServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("export")
public class ReportController {
    @Autowired
    private ReportServiceImpl reportService;

    @GetMapping("/report/{format}")
    public ResponseEntity<byte[]> generateReport(@PathVariable String format) {
        ReportDTO report = reportService.exportReport(format);
        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + report.getFilename())
                .contentType(MediaType.parseMediaType(report.getContentType()))
                .body(report.getData());
    }
}
