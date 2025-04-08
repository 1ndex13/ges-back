package com.example.demo.controller;

import com.example.demo.dto.OrderRequest;
import com.example.demo.model.Order;
import com.example.demo.model.User;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.EmailService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final EmailService emailService;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    public OrderController(EmailService emailService, OrderRepository orderRepository, UserRepository userRepository) {
        this.emailService = emailService;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }

    // Метод для создания заказа с проверкой на дублирование
    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody OrderRequest orderRequest, Principal principal) {
        try {
            User user = userRepository.findByUsername(principal.getName())
                    .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

            // Проверка на существующий заказ той же услуги для этого пользователя
            boolean alreadyOrdered = orderRepository.existsByUserAndService(user, orderRequest.getService());
            if (alreadyOrdered) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Вы уже заказали эту услугу");
            }

            // Создание нового заказа
            Order order = new Order();
            order.setService(orderRequest.getService());
            order.setUser(user);
            order.setUserName(orderRequest.getFirstName() + " " + orderRequest.getLastName());
            orderRepository.save(order);

            // Отправка email (если нужно)
            emailService.sendOrderEmail(orderRequest);

            return ResponseEntity.ok().body("Заказ успешно отправлен");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка: " + e.getMessage());
        }
    }

    // Метод для получения заказов текущего пользователя
    @GetMapping("/my")
    public ResponseEntity<List<Order>> getMyOrders(Principal principal) {
        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        List<Order> orders = orderRepository.findByUser(user);
        return ResponseEntity.ok(orders);
    }
}