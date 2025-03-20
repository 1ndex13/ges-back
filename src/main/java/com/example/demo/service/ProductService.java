package com.example.demo.service;

import com.example.demo.model.Product;
import com.example.demo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // Получить все товары
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // Получить товар по ID
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    // Добавить новый товар
    public Product addProduct(Product product) {
        return productRepository.save(product);
    }

    // Обновить существующий товар
    public Product updateProduct(Long id, Product updatedProduct) {
        return productRepository.findById(id)
                .map(product -> {
                    product.setTitle(updatedProduct.getTitle());
                    product.setDescription(updatedProduct.getDescription());
                    product.setImgSrc(updatedProduct.getImgSrc());
                    product.setTargetUrl(updatedProduct.getTargetUrl());
                    return productRepository.save(product);
                })
                .orElseThrow(() -> new RuntimeException("Товар не найден"));
    }

    // Удалить товар по ID
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}