package com.example.demo.repository;

import com.example.demo.model.Order;
import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    // Проверка существования заказа по пользователю и услуге
    boolean existsByUserAndService(User user, String service);

    // Поиск всех заказов пользователя
    List<Order> findByUser(User user);
}