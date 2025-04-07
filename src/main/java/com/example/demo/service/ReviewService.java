package com.example.demo.service;

import com.example.demo.dto.ReviewDto;
import com.example.demo.model.Product;
import com.example.demo.model.Review;
import com.example.demo.model.User;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.ReviewRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public ReviewDto addReview(String username, Long productId, String comment) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Услуга не найдена"));

        Review review = new Review();
        review.setUser(user);
        review.setProduct(product);
        review.setComment(comment);

        Review savedReview = reviewRepository.save(review);
        return mapToReviewDto(savedReview);
    }

    public List<ReviewDto> getReviewsByProductId(Long productId) {
        return reviewRepository.findByProductId(productId).stream()
                .map(this::mapToReviewDto)
                .collect(Collectors.toList());
    }

    private ReviewDto mapToReviewDto(Review review) {
        ReviewDto dto = new ReviewDto();
        dto.setId(review.getId());
        dto.setUsername(review.getUser().getUsername());
        dto.setProductId(review.getProduct().getId());
        dto.setProductTitle(review.getProduct().getTitle());
        dto.setComment(review.getComment());
        dto.setCreatedAt(review.getCreatedAt());
        return dto;
    }
}