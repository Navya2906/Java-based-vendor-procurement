package com.example.spvms.dto;

import java.util.Set;

import jakarta.validation.constraints.*;

public class UserDto {

    // ❌ Swagger will NOT require this (auto-generated)
    private Long id;

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 100)
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 6)
    private String password;

    @NotBlank(message = "Email is required")
    @Email
    private String email;

    @NotNull(message = "Active status is required")
    private Boolean isActive;
    
    @NotNull(message = "Role is Required")
    private Set<String> roles;

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }

    public Set<String> getRoles() {  return roles;  }

    public void setRoles(Set<String> roles) {  this.roles = roles;  }
}
