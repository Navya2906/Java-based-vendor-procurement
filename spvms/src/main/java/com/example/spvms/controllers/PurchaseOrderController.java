package com.example.spvms.controllers;

import jakarta.validation.Valid;

import java.util.Map;

import org.springframework.web.bind.annotation.*;

import com.example.spvms.dto.PurchaseOrderItemDTO;
import com.example.spvms.model.PurchaseOrder;
import com.example.spvms.model.PurchaseOrderItem;
import com.example.spvms.service.PurchaseOrderService;

@RestController
@RequestMapping("/api/purchase-orders")
public class PurchaseOrderController {

    private final PurchaseOrderService service;

    public PurchaseOrderController(PurchaseOrderService service) {
        this.service = service;
    }

    // PR → PO
    /* ================= CREATE PO ================= */
    @PostMapping("/from-pr/{prId}")
    public PurchaseOrder createPO(@PathVariable Long prId) {
        return service.createPO(prId);
    }

    /* ================= GET PO ================= */
    @GetMapping("/{poId}")
    public PurchaseOrder getPO(@PathVariable Long poId) {
        return service.getPO(poId);
    }

    // Add Item
    @PostMapping("/{poId}/items")
    public PurchaseOrder addItem(
            @PathVariable Long poId,
            @Valid @RequestBody PurchaseOrderItemDTO dto) {
        return service.addItem(poId, dto);
    }

    /* ================= FULL UPDATE ITEM ================= */
    @PutMapping("/items/{itemId}")
    public PurchaseOrder updateItem(
            @PathVariable Long itemId,
            @RequestBody PurchaseOrderItem item) {
        return service.updateItem(itemId, item);
    }

    /* ================= PARTIAL UPDATE ITEM ================= */
    @PatchMapping("/items/{itemId}")
    public PurchaseOrder patchItem(
            @PathVariable Long itemId,
            @RequestBody Map<String, Object> updates) {
        return service.patchItem(itemId, updates);
    }

    // Deliver Item
    @PutMapping("/items/{itemId}/deliver")
    public String deliverItem(@PathVariable Long itemId) {
        service.deliverItem(itemId);
        return "Item Delivered";
    }

    /* ================= DELETE ITEM ================= */
    @DeleteMapping("/items/{itemId}")
    public PurchaseOrder deleteItem(@PathVariable Long itemId) {
        return service.deleteItem(itemId);
    }


    // Close PO
    @PutMapping("/{poId}/close")
    public String closePO(@PathVariable Long poId) {
        service.closePO(poId);
        return "PO Closed";
    }
}
