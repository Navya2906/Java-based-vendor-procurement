package com.example.spvms.dto;

import java.math.BigDecimal;

public class PurchaseOrderDTO {

    private Long prId;
    private BigDecimal subtotal;
    private BigDecimal taxAmount;
    private BigDecimal totalAmount;
    private String poStatus;

    public Long getPrId() { return prId; }
    public void setPrId(Long prId) { this.prId = prId; }

    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }

    public BigDecimal getTaxAmount() { return taxAmount; }
    public void setTaxAmount(BigDecimal taxAmount) { this.taxAmount = taxAmount; }

    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }

    public String getPoStatus() { return poStatus; }
    public void setPoStatus(String poStatus) { this.poStatus = poStatus; }
}
