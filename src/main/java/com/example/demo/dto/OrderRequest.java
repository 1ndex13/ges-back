package com.example.demo.dto;

// OrderRequest.java
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class OrderRequest {
    @NotBlank(message = "Фамилия обязательна")
    private String lastName;

    @NotBlank(message = "Имя обязательно")
    private String firstName;

    @NotBlank(message = "Телефон обязателен")
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Неверный формат телефона")
    private String phone;

    @NotBlank(message = "Email обязателен")
    @Email(message = "Неверный формат email")
    private String email;

    @NotBlank(message = "Название услуги обязательно")
    private String service;
}

