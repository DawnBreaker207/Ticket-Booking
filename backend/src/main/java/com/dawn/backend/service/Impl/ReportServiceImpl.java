package com.dawn.backend.service.Impl;

import com.dawn.backend.dto.response.ReportResponseDTO;
import com.dawn.backend.model.Reservation;
import com.dawn.backend.repository.ReservationRepository;
import com.dawn.backend.service.ReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleHtmlExporterOutput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReportServiceImpl implements ReportService {

    private final ReservationRepository reservationRepository;

    @Override
    public ReportResponseDTO exportReport(String reportFormat) {
        try {
            List<Reservation> reservations = reservationRepository.findAll();
            if (reservations.isEmpty()) {
                return null;
            }

//        Load file and compile
            InputStream reportStream = new ClassPathResource("/report.jrxml").getInputStream();
            log.info("report stream: {}", reportStream);
            JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("createdBy", "Java Techie");
            parameters.put("REPORT_DATA", reservations);

//            Fill data
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters);

            byte[] reportBytes;
            String filename;
            String contentType;

//            Export by format
            if ("html".equalsIgnoreCase(reportFormat)) {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                HtmlExporter exporter = new HtmlExporter();
                exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                exporter.setExporterOutput(new SimpleHtmlExporterOutput(out));
                exporter.exportReport();
                reportBytes = out.toByteArray();
                filename = "report.html";
                contentType = "text/html";
            } else if ("pdf".equalsIgnoreCase(reportFormat)) {
                reportBytes = JasperExportManager.exportReportToPdf(jasperPrint);
                filename = "report.pdf";
                contentType = "application/pdf";
            } else if ("excel".equalsIgnoreCase(reportFormat)) {
                ByteArrayOutputStream out = new ByteArrayOutputStream();

                JRXlsxExporter exporter = new JRXlsxExporter();
                exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(out));

                SimpleXlsxReportConfiguration config = new SimpleXlsxReportConfiguration();
                config.setOnePagePerSheet(false);
                config.setDetectCellType(true);
                config.setCollapseRowSpan(false);
                exporter.setConfiguration(config);

                exporter.exportReport();
                exporter.reset();

                reportBytes = out.toByteArray();
                filename = "report.xlsx";
                contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

                out.close();
            } else {
                throw new IllegalArgumentException("Unsupported report format: " + reportFormat);
            }

            return new ReportResponseDTO(reportBytes, filename, contentType);
        } catch (JRException | IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
