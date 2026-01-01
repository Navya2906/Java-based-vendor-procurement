package com.example.spvms.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.spvms.dto.PurchaseOrderDto;
import com.example.spvms.model.PurchaseOrder;
import com.example.spvms.repository.PurchaseOrderRepository;
import com.example.spvms.repository.VendorRepository;

@Service
public class PurchaseOrderService {

    private final PurchaseOrderRepository poRepository;
    private final VendorRepository vendorRepository;

    public PurchaseOrderService(PurchaseOrderRepository poRepository,
                                VendorRepository vendorRepository) {
        this.poRepository = poRepository;
        this.vendorRepository = vendorRepository;
    }

    /* ================= CREATE ================= */

    public PurchaseOrder create(PurchaseOrderDto dto) {

        // ✅ Vendor validation (VERY IMPORTANT)
        if (!vendorRepository.existsById(dto.getVendorId())) {
            throw new IllegalArgumentException(
                    "Vendor with ID " + dto.getVendorId() + " does not exist");
        }

        PurchaseOrder po = new PurchaseOrder();
        po.setPoNumber(dto.getPoNumber());
        po.setPoDate(dto.getPoDate());
        po.setStatus(dto.getStatus());
        po.setTotalAmount(dto.getTotalAmount());
        po.setVendorId(dto.getVendorId());

        return poRepository.save(po);
    }

    /* ================= GET ALL ================= */

    public List<PurchaseOrder> getAll() {
        return poRepository.findAll();
    }

    /* ================= GET BY ID ================= */

    public PurchaseOrder getById(Long id) {
        return poRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Purchase Order not found with id: " + id));
    }

    /* ================= UPDATE STATUS ================= */

    public PurchaseOrder updateStatus(Long id, String status) {

        PurchaseOrder po = getById(id);

        // ✅ Status validation
        if (!isValidStatus(status)) {
            throw new IllegalArgumentException(
                    "Invalid status. Allowed values: DRAFT, SUBMITTED, APPROVED, REJECTED");
        }

        po.setStatus(status);
        return poRepository.save(po);
    }

    /* ================= DELETE ================= */

    public void delete(Long id) {

        PurchaseOrder po = getById(id); // ensures 404 if not exists
        poRepository.delete(po);
    }

    /* ================= STATUS CHECK ================= */

    private boolean isValidStatus(String status) {
        return status.equalsIgnoreCase("DRAFT")
                || status.equalsIgnoreCase("SUBMITTED")
                || status.equalsIgnoreCase("APPROVED")
                || status.equalsIgnoreCase("REJECTED");
    }
}
