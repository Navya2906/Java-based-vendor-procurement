package com.example.spvms.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;



@Entity
@Table(name = "purchase_requisitions")
public class PurchaseRequisition {

    /* ================= STATUS ENUM ================= */
    public enum Status {
        DRAFT,
        SUBMITTED,
        APPROVED,
        REJECTED;
    }

    /* ================= PRIMARY KEY ================= */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /* ================= BUSINESS FIELDS ================= */

    @Column(name = "pr_number", nullable = false, unique = true)
    @NotBlank(message = "PR number is required")
    private String prNumber;

    @Column(name = "requisition_number", nullable = false, unique = true)
    @NotBlank(message = "Requisition number is required")
    private String requisitionNumber;

    @Column(name = "requester_id", nullable = false)
    @NotNull(message = "Requester ID is required")
    private Long requesterId;

    @Column(name = "vendor_id")
    private Long vendorId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @Column(name = "total_amount", nullable = false)
    @NotNull(message = "Total amount is required")
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal totalAmount;

    @Column(nullable = false)
    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    @Column(length = 255)
    private String description;

    @Column(name = "requisition_date", nullable = false)
    @NotNull(message = "Requisition date is required")
    private LocalDate requisitionDate;


    /* ================= WORKFLOW FIELDS ================= */

    @Column(name = "approval_level", nullable = false)
    private Integer approvalLevel = 0;

    @Column(name = "last_action_by")
    private Long lastActionBy;

    @Column(name = "last_action_at")
    private LocalDateTime lastActionAt;

    @OneToMany(
        mappedBy = "purchaseRequisition",
        cascade = CascadeType.ALL,
        orphanRemoval = true,
        fetch = FetchType.LAZY
    )
    private List<PurchaseRequisitionItem> items = new ArrayList<>();


    /* ================= APPROVAL HISTORY ================= */

    @OneToMany(
        mappedBy = "purchaseRequisition",
        cascade = CascadeType.ALL,
        orphanRemoval = true,
        fetch = FetchType.LAZY
    )
    @JsonIgnore
    private List<PurchaseRequisitionApproval> approvalHistory;


    /* ================= AUDIT FIELDS ================= */

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;


    /* ================= JPA CALLBACKS ================= */

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();

        if (this.status == null) {
            this.status = Status.DRAFT;
        }
        if (this.approvalLevel == null) {
            this.approvalLevel = 0;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    /* ================= GETTERS & SETTERS ================= */

    public Long getId() {
        return id;
    }

    public String getPrNumber() {
        return prNumber;
    }

    public void setPrNumber(String prNumber) {
        this.prNumber = prNumber;
    }

    public String getRequisitionNumber() {
        return requisitionNumber;
    }

    public void setRequisitionNumber(String requisitionNumber) {
        this.requisitionNumber = requisitionNumber;
    }

    public Long getRequesterId() {
        return requesterId;
    }

    public void setRequesterId(Long requesterId) {
        this.requesterId = requesterId;
    }

    public Long getVendorId() {
        return vendorId;
    }

    public void setVendorId(Long vendorId) {
        this.vendorId = vendorId;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getRequisitionDate() {
        return requisitionDate;
    }

    public void setRequisitionDate(LocalDate requisitionDate) {
        this.requisitionDate = requisitionDate;
    }

    public Integer getApprovalLevel() {
        return approvalLevel;
    }

    public void setApprovalLevel(Integer approvalLevel) {
        this.approvalLevel = approvalLevel;
    }

    public Long getLastActionBy() {
        return lastActionBy;
    }

    public void setLastActionBy(Long lastActionBy) {
        this.lastActionBy = lastActionBy;
    }

    public LocalDateTime getLastActionAt() {
        return lastActionAt;
    }

    public void setLastActionAt(LocalDateTime lastActionAt) {
        this.lastActionAt = lastActionAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public List<PurchaseRequisitionApproval> getApprovalHistory() {
        return approvalHistory;
    }

    public List<PurchaseRequisitionItem> getItems() {
        return items;
    }

    public void setItems(List<PurchaseRequisitionItem> items) {
        this.items = items;
    }
}
