package com.example.spvms.service;

import com.example.spvms.model.PurchaseRequisition;
import com.example.spvms.repository.PurchaseRequisitionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class PurchaseRequisitionService {

    private final PurchaseRequisitionRepository repository;

    public PurchaseRequisitionService(PurchaseRequisitionRepository repository) {
        this.repository = repository;
    }

    /* ================= CREATE ================= */
    public PurchaseRequisition create(PurchaseRequisition pr) {

        if (repository.existsByPrNumber(pr.getPrNumber())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "PR number already exists"
            );
        }

        if (repository.existsByPrNumber(pr.getRequisitionNumber())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Requisition number already exists"
            );
        }

        return repository.save(pr);
    }

    /* ================= GET ALL ================= */
    public List<PurchaseRequisition> getAll() {
        return repository.findAll();
    }

    /* ================= GET BY ID ================= */
    public PurchaseRequisition getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Purchase Requisition not found with id: " + id
                ));
    }

    /* ================= UPDATE ================= */
    public PurchaseRequisition update(Long id, PurchaseRequisition updated) {

        PurchaseRequisition existing = getById(id);

        if (updated.getStatus() != null) {
            existing.setStatus(updated.getStatus());
        }

        if (updated.getQuantity() != null) {
            existing.setQuantity(updated.getQuantity());
        }

        if (updated.getTotalAmount() != null) {
            existing.setTotalAmount(updated.getTotalAmount());
        }

        if (updated.getVendorId() != null) {
            existing.setVendorId(updated.getVendorId());
        }

        if (updated.getDescription() != null) {
            existing.setDescription(updated.getDescription());
        }

        if (updated.getRequisitionDate() != null) {
            existing.setRequisitionDate(updated.getRequisitionDate());
        }

        return repository.save(existing);
    }

    /* ================= DELETE ================= */
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Purchase Requisition not found with id: " + id
            );
        }
        repository.deleteById(id);
    }
}
