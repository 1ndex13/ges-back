package com.example.demo.service;

import com.example.demo.model.Order;
import com.example.demo.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    // Метод для получения статистики заказов по услугам
    public Map<String, Long> getOrderStatisticsByService() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .collect(Collectors.groupingBy(Order::getService, Collectors.counting()));
    }

    // Другие методы сервиса, если они есть
}