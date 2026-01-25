package com.example.spvms.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.spvms.dto.UserDto;
import com.example.spvms.dto.UserResponse;
import com.example.spvms.model.Role;
import com.example.spvms.model.User;
import com.example.spvms.repository.RoleRepository;
import com.example.spvms.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                    RoleRepository roleRepository,
                    PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /* CREATE */
    public User create(UserDto dto) {

        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());
        user.setEmail(dto.getEmail());
        user.setIsActive(dto.getIsActive());

        return userRepository.save(user);
    }

    /* GET ALL */
    public List<User> getAll() {
        return userRepository.findAll();
    }

    /* GET BY ID */
    public User getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    /* UPDATE */
    public User update(Long id, UserDto dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setIsActive(dto.getIsActive());

        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        // ✅ FIX: ROLE HANDLING
        if (dto.getRoles() != null && !dto.getRoles().isEmpty()) {

            Set<Role> mappedRoles = new HashSet<>();

            for (String roleName : dto.getRoles()) {

                String finalRole = roleName.toUpperCase();

                Role role = roleRepository.findByName(finalRole)
                        .orElseThrow(() ->
                                new RuntimeException("Role not found: " + finalRole));

                mappedRoles.add(role);
            }

            user.setRoles(mappedRoles);
        }

        return userRepository.save(user);
    }

    /* UPDATE STATUS */
    public User updateStatus(Long id, Boolean status) {
        User user = getById(id);
        user.setIsActive(status);
        return userRepository.save(user);
    }

    /* DELETE (HARD DELETE) */
    @Transactional
    public void delete(Long id) {

        if (!userRepository.existsById(id)) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "User not found"
            );
        }

        userRepository.deleteById(id);
        userRepository.flush();
    }


    private UserResponse mapToResponse(User user) {
    return new UserResponse(
            user.getId(),
            user.getUsername(),
            user.getEmail(),
            user.getIsActive(),
            user.getRoles()
    );
}

}
