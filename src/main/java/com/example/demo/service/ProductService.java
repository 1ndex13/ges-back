package com.example.demo.service;

import com.example.demo.model.Product;
import com.example.demo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final Path uploadPath;

    @Autowired
    public ProductService(ProductRepository productRepository) throws IOException {
        this.productRepository = productRepository;
        this.uploadPath = Paths.get("uploads").toAbsolutePath().normalize();
        Files.createDirectories(uploadPath);
    }


    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public Product addProduct(Product product, MultipartFile image) throws IOException {
        if (image != null && !image.isEmpty()) {
            String imageUrl = saveImage(image);
            product.setImgSrc(imageUrl);
        }
        return productRepository.save(product);
    }

    public Product updateProduct(Long id, Product updatedProduct, MultipartFile image) throws IOException {
        return productRepository.findById(id)
                .map(product -> {
                    product.setTitle(updatedProduct.getTitle());
                    product.setDescription(updatedProduct.getDescription());

                    if (image != null && !image.isEmpty()) {
                        try {
                            String imageUrl = saveImage(image);
                            product.setImgSrc(imageUrl);
                        } catch (IOException e) {
                            throw new RuntimeException("Ошибка при сохранении изображения", e);
                        }
                    }

                    return productRepository.save(product);
                })
                .orElseThrow(() -> new RuntimeException("Товар не найден"));
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    public String saveImage(MultipartFile image) throws IOException {
        String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
        Path targetPath = uploadPath.resolve(fileName);
        image.transferTo(targetPath);
        return "/uploads/" + fileName;
    }
}