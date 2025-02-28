package com.example.demo.service;

import com.example.demo.model.PasswordResetToken;
import com.example.demo.model.User;
import com.example.demo.repository.PasswordResetTokenRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    private JavaMailSender mailSender;

    public User registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public Optional<User> authenticateUser(String username, String password) {
        return userRepository.findByUsername(username)
                .filter(user -> passwordEncoder.matches(password, user.getPassword()));
    }

    public void createPasswordResetTokenForUser(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            String token = UUID.randomUUID().toString();
            PasswordResetToken myToken = new PasswordResetToken(token, user);
            passwordResetTokenRepository.save(myToken);

            String resetLink = "http://localhost:3000/reset-password?token=" + token;
            sendEmail(user.getEmail(), resetLink);
        }else {
            throw new RuntimeException("Пользователь вместе с email " + email + " не найден");
        }
    }

    private void sendEmail(String email, String resetLink) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Запрос на сброс пароля");
        message.setText("Чтобы сбросить свой пароль, перейдите по ссылке ниже:\n" + resetLink);
        mailSender.send(message);
    }

    public void resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token);
        if (resetToken != null && !resetToken.isExpired()) {
            User user = resetToken.getUser();
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
            passwordResetTokenRepository.delete(resetToken);
        } else {
            throw new RuntimeException("Недействительный или просроченный токен");
        }
    }
}