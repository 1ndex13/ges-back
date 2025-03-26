package com.example.demo.dto;

import lombok.Data;

import java.util.Set;

@Data
public class UserUpdateDto {
    private String username;
    private String email;
    private Set<String> roles;
    private Boolean active;
}