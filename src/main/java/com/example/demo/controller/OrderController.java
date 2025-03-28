package com.example.demo.controller;

import com.example.demo.dto.OrderRequest;
import com.example.demo.service.EmailService;
import jakarta.mail.AuthenticationFailedException;
import jakarta.mail.MessagingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final EmailService emailService;

    public OrderController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody OrderRequest orderRequest) {
        try {
            emailService.sendOrderEmail(orderRequest);
            return ResponseEntity.ok().body("Заказ успешно отправлен");
        } catch (AuthenticationFailedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Ошибка аутентификации: " + e.getMessage());
        } catch (MessagingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка отправки письма: " + e.getMessage());
        }
    }

}