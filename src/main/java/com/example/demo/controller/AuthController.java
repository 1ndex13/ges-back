package com.example.demo.controller;

import com.example.demo.dto.LoginRequest;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.service.AuthService;
import com.example.demo.service.CustomUserDetailsService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    public ResponseEntity<?> register(@RequestBody User user, HttpServletResponse httpResponse) {
        try {
            User registeredUser = authService.registerUser(user);
            String token = authService.generateToken(registeredUser);
            Map<String, Object> responseBody = new HashMap<>(); // Переименовано из response
            responseBody.put("success", true);
            responseBody.put("token", token);
            responseBody.put("username", registeredUser.getUsername());
            responseBody.put("email", registeredUser.getEmail());
            responseBody.put("roles", registeredUser.getRoles().stream().map(Role::getName).collect(Collectors.toList()));

            // Установка cookie с JWT
            Cookie jwtCookie = new Cookie("jwtToken", token);
            jwtCookie.setHttpOnly(true); // Защита от XSS
            jwtCookie.setSecure(false); // Используйте только с HTTPS
            jwtCookie.setPath("/"); // Доступно для всего приложения
            jwtCookie.setMaxAge(3600); // Срок действия 1 час
            httpResponse.addCookie(jwtCookie); // Используем httpResponse вместо response

            return ResponseEntity.ok(responseBody);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Ошибка регистрации: " + e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletResponse httpResponse) {
        try {
            Optional<User> userOpt = authService.authenticateUser(loginRequest.getUsername(), loginRequest.getPassword());
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                String token = authService.generateToken(user);
                Map<String, Object> responseBody = new HashMap<>(); // Переименовано из response
                responseBody.put("success", true);
                responseBody.put("username", user.getUsername());
                responseBody.put("roles", user.getRoles().stream().map(Role::getName).collect(Collectors.toList()));

                // Установка cookie с JWT
                Cookie jwtCookie = new Cookie("jwtToken", token);
                jwtCookie.setHttpOnly(true); // Защита от XSS
                jwtCookie.setSecure(false); // Используйте только с HTTPS
                jwtCookie.setPath("/"); // Доступно для всего приложения
                jwtCookie.setMaxAge(3600); // Срок действия 1 час
                httpResponse.addCookie(jwtCookie);

                return ResponseEntity.ok(responseBody);
            } else {
                return ResponseEntity.status(401).body(Map.of("success", false, "message", "Неверный логин или пароль"));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Ошибка авторизации: " + e.getMessage()));
        }
    }

    @GetMapping("/check")
    public ResponseEntity<?> checkAuth(HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Authentication: " + auth);
        if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {
            System.out.println("Principal: " + auth.getPrincipal());
            CustomUserDetailsService.CustomUserDetails userDetails =
                    (CustomUserDetailsService.CustomUserDetails) auth.getPrincipal();
            User user = userDetails.getUser();
            System.out.println("User extracted: " + user.getUsername());
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("username", user.getUsername());
            response.put("email", user.getEmail());
            response.put("roles", user.getRoles().stream().map(Role::getName).collect(Collectors.toList()));
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(401).body(Map.of("success", false, "message", "Не авторизован"));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("jwtToken", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(0); // Удаляем cookie
        response.addCookie(cookie);
        return ResponseEntity.ok(Map.of("success", true, "message", "Выход выполнен"));
    }
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> request) {
        try {
            String email = request.get("email");
            authService.processForgotPassword(email);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Код для сброса пароля отправлен на email"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }

    @PostMapping("/verify-reset-code")
    public ResponseEntity<?> verifyResetCode(@RequestBody Map<String, String> request) {
        try {
            String email = request.get("email");
            String code = request.get("code");
            authService.verifyResetCode(email, code);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Код подтвержден"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> request) {
        try {
            String email = request.get("email");
            String newPassword = request.get("newPassword");
            authService.resetPassword(email, newPassword);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Пароль успешно изменен"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }
}
