package com.example.demo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Data
public class UserCreateDto {
    @NotBlank
    private String username;
    @Email
    private String email;
    @Size(min = 6) private String password;
    private Set<String> roles;
}