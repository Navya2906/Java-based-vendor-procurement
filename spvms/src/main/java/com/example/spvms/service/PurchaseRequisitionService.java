package com.example.spvms.service;

import com.example.spvms.dto.PurchaseRequisitionApprovalDto;
import com.example.spvms.model.PurchaseRequisition;
import com.example.spvms.model.PurchaseRequisitionApproval;
import com.example.spvms.repository.PurchaseRequisitionApprovalRepository;
import com.example.spvms.repository.PurchaseRequisitionRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import com.example.spvms.dto.PurchaseRequisitionApprovalDto;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PurchaseRequisitionService {

    private final PurchaseRequisitionRepository repository;
    private final PurchaseRequisitionApprovalRepository approvalRepository;

    public PurchaseRequisitionService(
            PurchaseRequisitionRepository repository,
            PurchaseRequisitionApprovalRepository approvalRepository) {
        this.repository = repository;
        this.approvalRepository = approvalRepository;
    }

    /* ================= CREATE ================= */
    public PurchaseRequisition create(PurchaseRequisition pr) {

        if (repository.existsByPrNumber(pr.getPrNumber())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "PR number already exists"
            );
        }

        pr.setStatus(PurchaseRequisition.Status.DRAFT);
        pr.setApprovalLevel(0);

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
                        "Purchase Requisition not found"
                ));
    }

    /* ================= UPDATE ================= */
    public PurchaseRequisition update(Long id, PurchaseRequisition updated) {

        PurchaseRequisition pr = getById(id);

        if (pr.getStatus() != PurchaseRequisition.Status.DRAFT) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Only DRAFT PR can be updated"
            );
        }

        if (updated.getQuantity() != null)
            pr.setQuantity(updated.getQuantity());

        if (updated.getTotalAmount() != null)
            pr.setTotalAmount(updated.getTotalAmount());

        if (updated.getVendorId() != null)
            pr.setVendorId(updated.getVendorId());

        if (updated.getDescription() != null)
            pr.setDescription(updated.getDescription());

        return repository.save(pr);
    }

    /* ================= SUBMIT ================= */
    public void submitPR(Long prId, Long requesterId) {

        PurchaseRequisition pr = getById(prId);

        // ✅ Only DRAFT → SUBMITTED allowed
        if (pr.getStatus() != PurchaseRequisition.Status.DRAFT) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Only DRAFT PR can be submitted"
        );
    }

    pr.setStatus(PurchaseRequisition.Status.SUBMITTED);
    pr.setApprovalLevel(0);
    pr.setLastActionBy(requesterId);
    pr.setLastActionAt(LocalDateTime.now());

    repository.save(pr);
}


    /* ================= APPROVE ================= */
    public void approvePR(Long prId, Long approverId, String role, String comments) {

        PurchaseRequisition pr = getById(prId);

        if (pr.getStatus() != PurchaseRequisition.Status.SUBMITTED) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Only SUBMITTED PR can be approved"
            );
        }

        if (!role.equals("ROLE_ADMIN") && !role.equals("ROLE_PROCUREMENT")) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "You are not authorized to approve this PR"
            );
        }

        if (pr.getRequesterId().equals(approverId)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Requester cannot approve their own PR"
            );
        }

        PurchaseRequisitionApproval approval = new PurchaseRequisitionApproval();
        approval.setPurchaseRequisition(pr);
        approval.setApproverId(approverId);
        approval.setAction(PurchaseRequisitionApproval.Action.APPROVED);

        approval.setLevel(0); // ✅ REQUIRED FIX
        approval.setComments(
            comments != null ? comments : "Approved"
        );
        approval.setActionDate(LocalDateTime.now());

        approvalRepository.save(approval);

        pr.setStatus(PurchaseRequisition.Status.APPROVED);
        pr.setLastActionBy(approverId);
        pr.setLastActionAt(LocalDateTime.now());

        repository.save(pr);
    }


    /* ================= REJECT ================= */
    public void rejectPR(Long prId, Long approverId, String comments) {

        if (comments == null || comments.trim().isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Comments are required when rejecting a PR"
            );
        }

        PurchaseRequisition pr = getById(prId);

        if (pr.getStatus() != PurchaseRequisition.Status.SUBMITTED) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Only SUBMITTED PR can be rejected"
            );
        }

        if (pr.getRequesterId().equals(approverId)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Requester cannot reject their own PR"
            );
        }

        PurchaseRequisitionApproval approval = new PurchaseRequisitionApproval();
        approval.setPurchaseRequisition(pr);
        approval.setApproverId(approverId);
        approval.setAction(PurchaseRequisitionApproval.Action.REJECTED);

        approval.setLevel(0); // ✅ REQUIRED FIX
        approval.setComments(comments);
        approval.setActionDate(LocalDateTime.now());

        approvalRepository.save(approval);

        pr.setStatus(PurchaseRequisition.Status.REJECTED);
        pr.setLastActionBy(approverId);
        pr.setLastActionAt(LocalDateTime.now());

        repository.save(pr);
    }


    /* ================= HISTORY ================= */
    public List<PurchaseRequisitionApprovalDto> getHistory(
        Long prId,
        Long userId,
        String role) {

    PurchaseRequisition pr = getById(prId);

    // 🔐 Rule 1: Requester can view ONLY their own PR
    if (role.equals("ROLE_USER") &&
        !pr.getRequesterId().equals(userId)) {

        throw new ResponseStatusException(
                HttpStatus.FORBIDDEN,
                "You are not authorized to view this PR history"
        );
    }

    // 🔐 Rule 2: Admin / Procurement / Finance allowed
    if (!role.equals("ROLE_ADMIN")
            && !role.equals("ROLE_PROCUREMENT")
            && !role.equals("ROLE_FINANCE")
            && !pr.getRequesterId().equals(userId)) {

        throw new ResponseStatusException(
                HttpStatus.FORBIDDEN,
                "You are not authorized to view this PR history"
        );
    }

    return approvalRepository
            .findByPurchaseRequisition_Id(prId)
            .stream()
            .map(a -> new PurchaseRequisitionApprovalDto(
                    a.getApproverId(),
                    a.getAction().name(),
                    a.getComments(),
                    a.getActionDate()
            ))
            .toList();
    }
}
