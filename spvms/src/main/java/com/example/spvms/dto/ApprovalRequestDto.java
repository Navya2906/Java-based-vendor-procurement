package com.example.spvms.dto;

import jakarta.validation.constraints.NotBlank;

public class ApprovalRequestDto {

    @NotBlank(message = "Comments are required")
    private String comments;

    public String getComments() { return comments; }
    public void setComments(String comments) { this.comments = comments; }
}
