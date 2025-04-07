package com.example.demo.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReviewDto {
    private Long id;
    private String username;
    private Long productId;
    private String productTitle;
    private String comment;
    private LocalDateTime createdAt;
}