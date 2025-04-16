package com.example.demo.repository;

import com.example.demo.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends BaseRepository<Product, Long> {
    List<Product> findByCategory(String category);
    // Здесь можно добавить дополнительные методы для поиска, если это необходимо
}