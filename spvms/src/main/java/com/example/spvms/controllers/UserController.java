package com.example.spvms.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.spvms.dto.UserDto;
import com.example.spvms.model.User;
import com.example.spvms.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    /* CREATE */
    @PostMapping
    public ResponseEntity<User> create(@Valid @RequestBody UserDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    /* GET ALL */
    @GetMapping
    public List<User> getAll() {
        return service.getAll();
    }

    /* GET BY ID */
    @GetMapping("/{id}")
    public User getById(@PathVariable Long id) {
        return service.getById(id);
    }

    /* UPDATE */
    @PutMapping("/{id}")
    public User update(@PathVariable Long id,
                       @Valid @RequestBody UserDto dto) {
        return service.update(id, dto);
    }

    /* UPDATE STATUS */
    @PatchMapping("/{id}/status")
    public User updateStatus(@PathVariable Long id,
                             @RequestParam Boolean isActive) {
        return service.updateStatus(id, isActive);
    }

    /* DELETE */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
