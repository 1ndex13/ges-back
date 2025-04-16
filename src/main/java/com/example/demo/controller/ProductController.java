package com.example.demo.controller;

import com.example.demo.model.Product;
import com.example.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return productService.getProductById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Product> addProduct(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("category") String category,
            @RequestParam(value = "image", required = false) MultipartFile image) {
        System.out.println("Title: " + title);
        System.out.println("Description: " + description);
        System.out.println("Image: " + (image != null ? image.getOriginalFilename() : "null"));
        try {
            Product product = new Product();
            product.setTitle(title);
            product.setDescription(description);
            product.setCategory(category);
            Product savedProduct = productService.addProduct(product, image);
            return ResponseEntity.ok(savedProduct);
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Product> updateProduct(
            @PathVariable Long id,
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("category") String category,  // New parameter
            @RequestParam(value = "image", required = false) MultipartFile image) {
        try {
            Product updatedProduct = new Product();
            updatedProduct.setTitle(title);
            updatedProduct.setDescription(description);
            updatedProduct.setCategory(category);  // Set category
            Product product = productService.updateProduct(id, updatedProduct, image);
            return ResponseEntity.ok(product);
        } catch (RuntimeException | IOException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}