package com.example.spvms.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.spvms.model.PurchaseOrder;

public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {
}
