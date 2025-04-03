package com.example.demo.controller;

import com.example.demo.mapper.EntityMapper;
import com.example.demo.service.BaseService;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.stream.Collectors;

public abstract class BaseController<D, E, ID> {
    protected final BaseService<E, ID> service;
    protected final EntityMapper<D, E> mapper;

    protected BaseController(BaseService<E, ID> service, EntityMapper<D, E> mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    protected ResponseEntity<List<D>> getAll() {
        return ResponseEntity.ok(
                service.findAll().stream()
                        .map(mapper::toDto)
                        .collect(Collectors.toList())
        );
    }
}