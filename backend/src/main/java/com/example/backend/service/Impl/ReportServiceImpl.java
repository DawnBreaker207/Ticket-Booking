package com.example.backend.service.Impl;

import com.example.backend.dto.response.ReportDTO;
import com.example.backend.model.Order;
import com.example.backend.repository.OrderRepository;
import com.example.backend.service.ReportService;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleHtmlExporterOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportServiceImpl implements ReportService {

    private static final Logger log = LoggerFactory.getLogger(ReportServiceImpl.class);
    @Autowired
    private OrderRepository orderRepository;

    @Override
    public ReportDTO exportReport(String reportFormat) {
        try {
            List<Order> orders = orderRepository.findAll();
            if (orders.isEmpty()) {
                return null;
            }

//        Load file and compile
            InputStream reportStream = new ClassPathResource("/report.jrxml").getInputStream();
            log.info("report stream: {}", reportStream);
            JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("createdBy", "Java Techie");
            parameters.put("REPORT_DATA", orders);

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
            } else {
                throw new IllegalArgumentException("Unsupported report format: " + reportFormat);
            }

            return new ReportDTO(reportBytes, filename, contentType);
        } catch (JRException ex) {
            throw new RuntimeException(ex);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
