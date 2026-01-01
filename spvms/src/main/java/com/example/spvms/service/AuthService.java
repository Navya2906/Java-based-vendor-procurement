package com.example.spvms.service;

import com.example.spvms.dto.RegisterRequest;
import com.example.spvms.dto.RegisterResponse;
import com.example.spvms.model.Role;
import com.example.spvms.model.User;
import com.example.spvms.repository.RoleRepository;
import com.example.spvms.repository.UserRepository;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository,
                       RoleRepository roleRepository) {

        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    /* ================= REGISTER USER ================= */

    public RegisterResponse register(RegisterRequest request) {

        // Duplicate checks
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        // Create User
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setIsActive(true);

        // Assign roles
        Set<Role> roles = new HashSet<>();

        if (request.getRoles() != null && !request.getRoles().isEmpty()) {
            for (String roleName : request.getRoles()) {
                Role role = roleRepository.findByName(roleName)
                        .orElseThrow(() ->
                                new RuntimeException("Role not found: " + roleName));
                roles.add(role);
            }
        } else {
            // Default role
            Role defaultRole = roleRepository.findByName("VENDOR")
                    .orElseThrow(() ->
                            new RuntimeException("Default role VENDOR not found"));
            roles.add(defaultRole);
        }

        user.setRoles(roles);

        // Save user
        User savedUser = userRepository.save(user);

        // Response
        return new RegisterResponse(
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getEmail()
        );
    }
}
