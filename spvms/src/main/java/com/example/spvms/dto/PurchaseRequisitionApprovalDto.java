package com.example.spvms.dto;

import java.time.LocalDateTime;

public class PurchaseRequisitionApprovalDto {

    private Long approverId;
    private String action;
    private String comments;
    private LocalDateTime actionDate;

    public PurchaseRequisitionApprovalDto(
            Long approverId,
            String action,
            String comments,
            LocalDateTime actionDate) {
        this.approverId = approverId;
        this.action = action;
        this.comments = comments;
        this.actionDate = actionDate;
    }

    public Long getApproverId() {
        return approverId;
    }

    public String getAction() {
        return action;
    }

    public String getComments() {
        return comments;
    }

    public LocalDateTime getActionDate() {
        return actionDate;
    }
}
