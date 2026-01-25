package com.example.spvms.repository;

import com.example.spvms.model.PurchaseRequisitionApproval;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

public interface PurchaseRequisitionApprovalRepository
        extends JpaRepository<PurchaseRequisitionApproval, Long> {

    List<PurchaseRequisitionApproval> findByPurchaseRequisition_Id(Long prId);
}
