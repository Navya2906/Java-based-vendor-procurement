package com.example.spvms.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.spvms.dto.PurchaseOrderDto;
import com.example.spvms.model.PurchaseOrder;
import com.example.spvms.service.PurchaseOrderService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/purchase-orders")
public class PurchaseOrderController {

    private final PurchaseOrderService service;

    public PurchaseOrderController(PurchaseOrderService service) {
        this.service = service;
    }

    /* ================= CREATE PO ================= */

    @PostMapping
    public ResponseEntity<PurchaseOrder> createPurchaseOrder(
            @Valid @RequestBody PurchaseOrderDto dto) {

        PurchaseOrder created = service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /* ================= GET ALL PO ================= */

    @GetMapping
    public ResponseEntity<List<PurchaseOrder>> getAllPurchaseOrders() {
        return ResponseEntity.ok(service.getAll());
    }

    /* ================= GET PO BY ID ================= */

    @GetMapping("/{id}")
    public ResponseEntity<Object> getPurchaseOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    /* ================= UPDATE STATUS ================= */

    @PatchMapping("/{id}/status")
    public ResponseEntity<PurchaseOrder> updateStatus(
            @PathVariable Long id,
            @RequestParam String status) {

        return ResponseEntity.ok(service.updateStatus(id, status));
    }

    /* ================= DELETE PO ================= */

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePurchaseOrder(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
