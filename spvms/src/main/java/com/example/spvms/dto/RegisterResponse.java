package com.example.spvms.dto;

public class RegisterResponse {

    private Long id;
    private String username;
    private String email;
    private String message;


    public RegisterResponse() {}

    public RegisterResponse(Long id, String username, String email, String message) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.message=message;
    }

    // ===== Getters & Setters =====
    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getMessage() {
        return message;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}