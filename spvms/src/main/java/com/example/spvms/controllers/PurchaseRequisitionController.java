package com.example.spvms.controllers;

import com.example.spvms.model.PurchaseRequisition;
import com.example.spvms.service.PurchaseRequisitionService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/purchase-requisitions")
public class PurchaseRequisitionController {

    private final PurchaseRequisitionService service;

    public PurchaseRequisitionController(PurchaseRequisitionService service) {
        this.service = service;
    }

    @PostMapping
    public PurchaseRequisition create(@Valid @RequestBody PurchaseRequisition pr) {
        return service.create(pr);
    }

    @GetMapping
    public List<PurchaseRequisition> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public PurchaseRequisition getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PutMapping("/{id}")
    public PurchaseRequisition update(
            @PathVariable Long id,
            @Valid @RequestBody PurchaseRequisition pr) {
        return service.update(id, pr);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
