package com.example.spvms.controllers;

import com.example.spvms.dto.VendorRequest;
import com.example.spvms.model.Vendor;
import com.example.spvms.service.VendorService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vendors")
public class VendorController {

    @Autowired
    private VendorService vendorService;

    @PostMapping
    public Vendor createVendor(@Valid @RequestBody VendorRequest request) {
        return vendorService.createVendor(request);
    }

    @GetMapping
    public List<Vendor> getAllVendors() {
        return vendorService.getAllVendors();
    }

    @GetMapping("/{id}")
    public Vendor getVendor(@PathVariable Long id) {
        return vendorService.getVendorById(id);
    }

    @PutMapping("/{id}")
    public Vendor updateVendor(
            @PathVariable Long id,
            @Valid @RequestBody VendorRequest request) {
        return vendorService.updateVendor(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteVendor(@PathVariable Long id) {
        vendorService.deleteVendor(id);
    }

    // 🔥 Sprint-3 Search API
    @GetMapping("/search")
    public Page<Vendor> searchVendors(
            @RequestParam(required = false) Double rating,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Boolean compliance,
            Pageable pageable) {

        return vendorService.searchVendors(
                rating, location, category, compliance, pageable);
    }
}
