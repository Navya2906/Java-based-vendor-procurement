package com.example.spvms.controllers;

import com.example.spvms.dto.PurchaseRequisitionApprovalDto;
import com.example.spvms.model.PurchaseRequisition;
import com.example.spvms.model.PurchaseRequisitionApproval;
import com.example.spvms.dto.PurchaseRequisitionApprovalDto;
import com.example.spvms.service.PurchaseRequisitionService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/purchase-requisitions")
public class PurchaseRequisitionController {

    private final PurchaseRequisitionService service;

    public PurchaseRequisitionController(PurchaseRequisitionService service) {
        this.service = service;
    }

    /* ================= CREATE ================= */
    @PostMapping
    public ResponseEntity<PurchaseRequisition> create(
            @RequestBody PurchaseRequisition pr) {

        return ResponseEntity.ok(service.create(pr));
    }

    /* ================= GET ALL ================= */
    @GetMapping
    public ResponseEntity<List<PurchaseRequisition>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    /* ================= GET BY ID ================= */
    @GetMapping("/{id}")
    public ResponseEntity<PurchaseRequisition> getById(
            @PathVariable Long id) {

        return ResponseEntity.ok(service.getById(id));
    }

    /* ================= UPDATE ================= */
    @PutMapping("/{id}")
    public ResponseEntity<PurchaseRequisition> update(
            @PathVariable Long id,
            @RequestBody PurchaseRequisition pr) {

        return ResponseEntity.ok(service.update(id, pr));
    }

    /* ================= SUBMIT ================= */
    @PostMapping("/{id}/submit")
    public ResponseEntity<String> submit(
        @PathVariable Long id,
        Authentication authentication) {

        // ✅ CORRECT: JWT principal is userId
        Long requesterId = (Long) authentication.getPrincipal();

        service.submitPR(id, requesterId);

        return ResponseEntity.ok("Purchase Requisition submitted successfully");
    }

    /* ================= APPROVE ================= */
    @PostMapping("/{id}/approve")
    public ResponseEntity<String> approve(
            @PathVariable Long id,
            Authentication authentication,
            @RequestParam(required = false) String comments) {

        // ✅ FIX: safe userId extraction
        Long approverId = Long.valueOf(authentication.getName());

        // ✅ FIX: safe role extraction
        String role = authentication.getAuthorities()
                .stream()
                .map(a -> a.getAuthority())
                .findFirst()
                .orElse("");

        service.approvePR(id, approverId, role, comments);

        return ResponseEntity.ok("Purchase Requisition approved successfully");
    }

    /* ================= REJECT ================= */
    @PostMapping("/{id}/reject")
    public ResponseEntity<String> reject(
            @PathVariable Long id,
            Authentication authentication,
            @RequestParam(required = false) String comments) {

        // ✅ FIX: safe userId extraction
        Long approverId = Long.valueOf(authentication.getName());

        service.rejectPR(id, approverId, comments);

        return ResponseEntity.ok("Purchase Requisition rejected successfully");
    }

    /* ================= HISTORY ================= */
    @GetMapping("/{id}/history")
    public ResponseEntity<List<PurchaseRequisitionApprovalDto>> history(
        @PathVariable Long id,
        Authentication authentication) {

            Long userId = Long.valueOf(authentication.getName());

            String role = authentication.getAuthorities()
                .stream()
                .map(a -> a.getAuthority())
                .findFirst()
                .orElse("");

        return ResponseEntity.ok(
                service.getHistory(id, userId, role)
        );
    }
}
