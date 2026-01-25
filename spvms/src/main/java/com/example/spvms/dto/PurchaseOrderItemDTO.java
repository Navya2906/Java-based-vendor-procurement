package com.example.spvms.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

import com.example.spvms.model.PurchaseOrder;

public class PurchaseOrderItemDTO {

    @NotBlank
    private String itemName;

    @NotNull @Positive
    private Integer quantity;

    @NotNull @Positive
    private BigDecimal unitPrice;

    @NotNull
    private BigDecimal discount;

    @NotNull @Positive
    private BigDecimal taxPercent;

    public String getItemName() { return itemName; }
    public void setItemName(@NotBlank String itemName) { this.itemName = itemName; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }

    public BigDecimal getDiscount() { return discount; }
    public void setDiscount(BigDecimal discount) { this.discount = discount; }

    public BigDecimal getTaxPercent() { return taxPercent; }
    public void setTaxPercent(BigDecimal taxPercent) { this.taxPercent = taxPercent; }
    public void setPurchaseOrder(PurchaseOrder po) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setPurchaseOrder'");
    }
    public void setItemName(BigDecimal bigDecimal) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setItemName'");
    }
}
