package com.example.demo.mapper;

import com.example.demo.dto.UserDto;
import com.example.demo.model.User;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;


@Component
public class UserMapper {

    public UserDto toDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setRoles(user.getRoles().stream()
                .map(role -> role.getName())
                .collect(Collectors.toSet()));
        return dto;
    }

    public User toEntity(UserDto dto) {
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        // Пароль должен устанавливаться отдельно
        return user;
    }
}
