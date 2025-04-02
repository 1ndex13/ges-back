package com.example.demo.controller;

import com.example.demo.dto.UserDto;
import com.example.demo.dto.UserProfileDTO;
import com.example.demo.mapper.UserMapper;
import com.example.demo.model.User;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    // Методы для обычных пользователей

    @PutMapping("/profile")
    public ResponseEntity<User> updateProfile(@RequestBody UserProfileDTO profileDTO, Principal principal) {
        User updatedUser = userService.updateProfile(principal.getName(), profileDTO);
        return ResponseEntity.ok(updatedUser);
    }

    @PostMapping("/upload-avatar")
    public ResponseEntity<?> uploadAvatar(@RequestParam("avatar") MultipartFile file, Principal principal) {
        String avatarUrl = userService.uploadAvatar(principal.getName(), file);
        return ResponseEntity.ok(Collections.singletonMap("avatarUrl", avatarUrl));
    }

    // Административные методы с ограничением доступа

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
        return ResponseEntity.ok(userMapper.toDto(userService.save(userMapper.toEntity(userDto))));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDto> updateUser(
            @PathVariable Long id,
            @RequestBody UserDto userDto) {
        User user = userMapper.toEntity(userDto);
        user.setActive(userDto.isActive()); // Сохранение статуса активности
        return ResponseEntity.ok(userMapper.toDto(userService.update(id, user)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDto>> searchUsers(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) Boolean active) {
        List<UserDto> users = userService.searchUsers(username, email, active);
        return ResponseEntity.ok(users);
    }
}