package com.example.backend.service;

import com.example.backend.util.BarcodeUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final JavaMailSender mailSender;

    private final TemplateEngine templateEngine;

    public void sendEmail(String to, String name, String reservationId) {
        log.info("Got message from reservation");
        String barcodeBase64 = BarcodeUtils.generateCode128(reservationId, 300, 100);

        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
            messageHelper.setFrom("demo@gmail.com");
            messageHelper.setTo(to);
            messageHelper.setSubject("[Thông tin vé phim] - Đặt vé trực tuyến thành công / Your online ticket purchase has been successful");

            Context context = new Context();
            context.setVariable("name", name);
            context.setVariable("reservationId", reservationId);
            context.setVariable("movieName", "Overlord");
            context.setVariable("theaterName", "Thanh Xuan");
            context.setVariable("showtimeSession", "11/10/2025 12:00:00");
            context.setVariable("seats", "A2,A3");
            context.setVariable("total", "100000");
            context.setVariable("barcode", barcodeBase64);

            String html = templateEngine.process("email", context);
            messageHelper.setText(html, true);
        };

        try {
            mailSender.send(messagePreparator);
            log.info("Email notification sent!");
        } catch (MailException ex) {
            log.error("Exception occurred when sending email", ex);
            throw new RuntimeException("Exception occurred when sending email to demo@gmail.com", ex);
        }

    }

}
