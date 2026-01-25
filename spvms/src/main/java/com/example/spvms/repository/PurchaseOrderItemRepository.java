package com.example.spvms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.spvms.dto.PurchaseOrderItemDTO;
import com.example.spvms.model.PurchaseOrder;
import com.example.spvms.model.PurchaseOrderItem;

public interface PurchaseOrderItemRepository extends JpaRepository<PurchaseOrderItem, Long> {

    List<PurchaseOrderItem> findByPurchaseOrder(PurchaseOrder po);

    void save(PurchaseOrderItemDTO dto);
}
