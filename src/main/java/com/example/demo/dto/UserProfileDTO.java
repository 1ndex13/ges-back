package com.example.demo.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UserProfileDTO {
    private String nickname;
    private String email;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private String avatar;
}