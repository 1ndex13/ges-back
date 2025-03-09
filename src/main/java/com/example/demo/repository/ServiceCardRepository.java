package com.example.demo.repository;

import com.example.demo.model.ServiceCard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceCardRepository extends JpaRepository<ServiceCard, Long> {
}