package com.example.spvms.dto;

import java.util.Set;
import com.example.spvms.model.Role;

public class UserResponse {

    private Long id;
    private String username;
    private String email;
    private Boolean isActive;
    private Set<Role> roles;

    // ----- Constructors -----
    public UserResponse() {}

    public UserResponse(Long id, String username, String email,
                        Boolean isActive, Set<Role> roles) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.isActive = isActive;
        this.roles = roles;
    }

    // ----- Getters & Setters -----
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}
