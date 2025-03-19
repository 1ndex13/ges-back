package com.example.demo.service;

import com.example.demo.model.Product;
import com.example.demo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    // Добавление товара
    public Product addProduct(Product product) {
        return productRepository.save(product);
    }

    // Получение всех товаров
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // Получение товара по ID
    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    // Удаление товара
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}