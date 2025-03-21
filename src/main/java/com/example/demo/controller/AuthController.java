package com.example.demo.controller;

import com.example.demo.dto.LoginRequest;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        try {
            User registeredUser = authService.registerUser(user);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("username", registeredUser.getUsername());
            response.put("email", registeredUser.getEmail());
            response.put("roles", registeredUser.getRoles().stream().map(Role::getName).collect(Collectors.toList()));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Ошибка регистрации: " + e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            Optional<User> user = authService.authenticateUser(loginRequest.getUsername(), loginRequest.getPassword());
            if (user.isPresent()) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("username", user.get().getUsername());
                response.put("roles", user.get().getRoles().stream().map(Role::getName).collect(Collectors.toList()));
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(401).body(Map.of("success", false, "message", "Неверный логин или пароль"));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Ошибка авторизации: " + e.getMessage()));
        }
    }
}

