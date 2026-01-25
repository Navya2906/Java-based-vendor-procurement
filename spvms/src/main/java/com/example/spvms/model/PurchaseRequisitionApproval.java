package com.example.spvms.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "purchase_requisition_approvals")
public class PurchaseRequisitionApproval {

    public enum Action {
        APPROVED,
        REJECTED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 🔗 Many approvals → One PR
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pr_id", nullable = false)
    @JsonIgnore
    private PurchaseRequisition purchaseRequisition;

    @Column(name = "approver_id", nullable = false)
    private Long approverId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Action action;

    @Column(nullable = false)
    private Integer level;

    @Column(length = 500)
    private String comments;

    @Column(name = "action_date", nullable = false)
    private LocalDateTime actionDate;

    /* ===== Getters & Setters ===== */

    public Long getId() { return id; }

    public PurchaseRequisition getPurchaseRequisition() {
        return purchaseRequisition;
    }

    public void setPurchaseRequisition(PurchaseRequisition purchaseRequisition) {
        this.purchaseRequisition = purchaseRequisition;
    }

    public Long getApproverId() { return approverId; }
    public void setApproverId(Long approverId) { this.approverId = approverId; }

    public Action getAction() { return action; }
    public void setAction(Action action) { this.action = action; }

    public Integer getLevel() { return level; }
    public void setLevel(Integer level) { this.level = level; }

    public String getComments() { return comments; }
    public void setComments(String comments) { this.comments = comments; }

    public LocalDateTime getActionDate() { return actionDate; }
    public void setActionDate(LocalDateTime actionDate) {
        this.actionDate = actionDate;
    }
}
