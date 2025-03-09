package com.example.demo.controller;

import com.example.demo.model.ServiceCard;
import com.example.demo.repository.ServiceCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/services")
public class ServiceCardController {

    @Autowired
    private ServiceCardRepository serviceCardRepository;

    // Получить все услуги
    @GetMapping
    public List<ServiceCard> getAllServices() {
        return serviceCardRepository.findAll();
    }

    // Получить услугу по ID
    @GetMapping("/{id}")
    public ServiceCard getServiceById(@PathVariable Long id) {
        return serviceCardRepository.findById(id).orElse(null);
    }

    // Создать новую услугу
    @PostMapping
    public ServiceCard createService(@RequestBody ServiceCard serviceCard) {
        return serviceCardRepository.save(serviceCard);
    }

    // Обновить услугу
    @PutMapping("/{id}")
    public ServiceCard updateService(@PathVariable Long id, @RequestBody ServiceCard updatedServiceCard) {
        ServiceCard serviceCard = serviceCardRepository.findById(id).orElse(null);
        if (serviceCard != null) {
            serviceCard.setTitle(updatedServiceCard.getTitle());
            serviceCard.setDescription(updatedServiceCard.getDescription());
            serviceCard.setPrice(updatedServiceCard.getPrice());
            serviceCard.setCategory(updatedServiceCard.getCategory());
            serviceCard.setImgSrc(updatedServiceCard.getImgSrc());
            return serviceCardRepository.save(serviceCard);
        }
        return null;
    }

    // Удалить услугу
    @DeleteMapping("/{id}")
    public void deleteService(@PathVariable Long id) {
        serviceCardRepository.deleteById(id);
    }
}