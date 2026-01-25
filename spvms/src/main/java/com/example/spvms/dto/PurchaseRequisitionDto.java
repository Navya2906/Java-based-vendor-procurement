package com.example.spvms.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;


public class PurchaseRequisitionDto {

    /* ================= STATUS ENUM ================= */
    public enum Status {
        DRAFT,
        SUBMITTED,
        APPROVED,
        REJECTED
    }

    @NotBlank(message = "PR number is required")
    private String prNumber;

    @NotNull(message = "Requester ID is required")
    private Long requesterId;

    @NotNull(message = "Vendor ID is required")
    private Long vendorId;

    @NotNull(message = "Status is required")
    private Status status;

    @NotNull(message = "Total amount is required")
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal totalAmount;

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    @NotNull(message = "Requisition date is required")
    private LocalDate requisitionDate;

    /* ================= Getters & Setters ================= */

    public String getPrNumber() { return prNumber; }
    public void setPrNumber(String prNumber) { this.prNumber = prNumber; }

    public Long getRequesterId() { return requesterId; }
    public void setRequesterId(Long requesterId) { this.requesterId = requesterId; }

    public Long getVendorId() { return vendorId; }
    public void setVendorId(Long vendorId) { this.vendorId = vendorId; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public LocalDate getRequisitionDate() { return requisitionDate; }
    public void setRequisitionDate(LocalDate requisitionDate) {
        this.requisitionDate = requisitionDate;
    }
}
