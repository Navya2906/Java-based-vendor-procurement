package com.example.spvms.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "purchase_requisitions")
public class PurchaseRequisition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    @Column(nullable = false)
    @NotBlank(message = "Status is required")
    private String status;

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

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // ---------- GETTERS & SETTERS ----------

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getPrNumber() { return prNumber; }
    public void setPrNumber(String prNumber) { this.prNumber = prNumber; }

    public String getRequisitionNumber() { return requisitionNumber; }
    public void setRequisitionNumber(String requisitionNumber) {
        this.requisitionNumber = requisitionNumber;
    }

    public Long getRequesterId() { return requesterId; }
    public void setRequesterId(Long requesterId) { this.requesterId = requesterId; }

    public Long getVendorId() { return vendorId; }
    public void setVendorId(Long vendorId) { this.vendorId = vendorId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDate getRequisitionDate() { return requisitionDate; }
    public void setRequisitionDate(LocalDate requisitionDate) {
        this.requisitionDate = requisitionDate;
    }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
