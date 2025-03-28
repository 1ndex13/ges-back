package com.example.demo.service;

import com.example.demo.dto.OrderRequest;
import jakarta.annotation.PostConstruct;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String smtpUsername;

    @Value("${spring.mail.password}")
    private String smtpPassword;

    @Value("${admin.email:admin@example.com}")
    private String adminEmail;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @PostConstruct
    public void init() {
        if (!StringUtils.hasText(smtpUsername) || !StringUtils.hasText(smtpPassword)) {
            throw new IllegalStateException("SMTP credentials not configured properly!");
        }
        System.out.println("SMTP configured for: " + smtpUsername);
        System.out.println("Admin email: " + adminEmail);
    }

    public void sendOrderEmail(OrderRequest order) throws MessagingException {
        if (order == null || !StringUtils.hasText(adminEmail)) {
            throw new IllegalArgumentException("Invalid order data or admin email not configured");
        }

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(smtpUsername);
            message.setTo(adminEmail);
            message.setSubject(buildSubject(order));
            message.setText(buildEmailContent(order));

            mailSender.send(message);
            System.out.println("Email successfully sent to: " + adminEmail);
        } catch (MailAuthenticationException e) {
            System.err.println("SMTP Authentication failed for user: " + smtpUsername);
            throw new MessagingException("SMTP authentication failed. Please check credentials", e);
        } catch (MailSendException e) {
            System.err.println("Email sending failed: " + e.getMessage());
            throw new MessagingException("Failed to send email. Please try again later", e);
        }
    }

    private String buildSubject(OrderRequest order) {
        return String.format("Новый заказ услуги: %s",
                order.getService() != null ? order.getService() : "Без названия");
    }

    private String buildEmailContent(OrderRequest order) {
        return String.format(
                "Детали заказа:\n\n" +
                        "Услуга: %s\n" +
                        "Клиент: %s %s\n" +
                        "Телефон: %s\n" +
                        "Email клиента: %s\n\n" +
                        "Дата: %s",
                order.getService() != null ? order.getService() : "Не указано",
                order.getLastName() != null ? order.getLastName() : "",
                order.getFirstName() != null ? order.getFirstName() : "",
                order.getPhone() != null ? order.getPhone() : "Не указан",
                order.getEmail() != null ? order.getEmail() : "Не указан",
                java.time.LocalDateTime.now()
        );
    }
}