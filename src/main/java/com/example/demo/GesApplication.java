package com.example.demo;

import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class GesApplication {

	public static void main(String[] args) {
		SpringApplication.run(GesApplication.class, args);
	}

	@Bean
	public CommandLineRunner initRolesAndAdmin(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
		return args -> {
			// Инициализация роли USER
			if (roleRepository.findByName("USER").isEmpty()) {
				Role userRole = new Role();
				userRole.setName("USER");
				roleRepository.save(userRole);
			}

			// Инициализация роли ADMIN
			Role adminRole = roleRepository.findByName("ADMIN")
					.orElseGet(() -> {
						Role newRole = new Role();
						newRole.setName("ADMIN");
						return roleRepository.save(newRole);
					});

			// Инициализация пользователя admin
			if (userRepository.findByUsername("admin").isEmpty()) {
				User admin = new User();
				admin.setUsername("admin");
				admin.setEmail("admin@example.com"); // Укажите email
				admin.setPassword(passwordEncoder.encode("admin123"));
				admin.setRole("ADMIN"); // Устанавливаем строковое поле role
				admin.getRoles().add(adminRole); // Добавляем роль в коллекцию roles
				userRepository.save(admin);
			}
		};
	}
}