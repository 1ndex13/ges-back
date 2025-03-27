package com.example.demo.service;

import com.example.demo.model.Product;
import com.example.demo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private static final String UPLOAD_DIR = "C:/uploads/"; // Укажите существующий путь

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
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
        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) {
            System.out.println("Creating directory: " + UPLOAD_DIR);
            uploadDir.mkdirs();
        }
        String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
        File destinationFile = new File(uploadDir, fileName);
        System.out.println("Saving to: " + destinationFile.getAbsolutePath());
        image.transferTo(destinationFile);
        return "/uploads/" + fileName;
    }
}