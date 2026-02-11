package com.example.spvms.service;

import org.springframework.stereotype.Service;

import com.example.spvms.dto.PurchaseOrderDTO;
import com.example.spvms.dto.PurchaseOrderItemDTO;
import com.example.spvms.model.PurchaseOrder;
import com.example.spvms.model.PurchaseOrderItem;
import com.example.spvms.model.PurchaseRequisition;
import com.example.spvms.model.PurchaseRequisition.Status;
import com.example.spvms.model.PurchaseRequisitionItem;
import com.example.spvms.repository.PurchaseOrderItemRepository;
import com.example.spvms.repository.PurchaseOrderRepository;
import com.example.spvms.repository.PurchaseRequisitionRepository;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.math.BigDecimal;

@Service
@Transactional
public class PurchaseOrderService {

    private final PurchaseOrderRepository poRepo;
    private final PurchaseOrderItemRepository itemRepo;
    private final PurchaseRequisitionRepository prRepo;

    public PurchaseOrderService(PurchaseOrderRepository poRepo,
                                PurchaseOrderItemRepository itemRepo,
                                PurchaseRequisitionRepository prRepo) {
        this.poRepo = poRepo;
        this.itemRepo = itemRepo;
        this.prRepo = prRepo;
    }

    // PR → PO
    public PurchaseOrder createFromPR(Long prId) {

        PurchaseRequisition pr = prRepo.findById(prId)
                .orElseThrow(() -> new RuntimeException("PR not found"));

        if (pr.getStatus() != Status.APPROVED) {
            throw new RuntimeException("PR not approved");
        }

        PurchaseOrder po = new PurchaseOrder();
        po.setPrId(prId);
        po.setPoDate(LocalDate.now());   // ✅ THIS FIXES ERROR
        po.setPoStatus("CREATED");
        po.setSubtotal(BigDecimal.ZERO);
        po.setTaxAmount(BigDecimal.ZERO);
        po.setTotalAmount(BigDecimal.ZERO);

        List<PurchaseOrderItem> poItems = new ArrayList<>();

        for (PurchaseRequisitionItem prItem : pr.getItems()) {

            PurchaseOrderItem poItem = new PurchaseOrderItem();
            poItem.setItemName(prItem.getItemName());
            poItem.setQuantity(prItem.getQuantity());
            poItem.setUnitPrice(prItem.getUnitPrice());
            poItem.setTaxPercent(prItem.getTaxPercent());
            poItem.setPurchaseOrder(po);

            poItems.add(poItem);
        }

        po.setItems(poItems);

        return poRepo.save(po);
    }

    // ✅ GET ALL
     public List<PurchaseOrder> getAllPOs() {
        List<PurchaseOrder> orders = poRepo.findAll();

        System.out.println("GET ALL POs → count = " + orders.size());

        return orders;
    }
    /* ================= GET PO ================= */
    public PurchaseOrder getPO(Long PoId) {
        return poRepo.findById(PoId)
                .orElseThrow(() -> new RuntimeException("PO not found"));
    }

    // ADD ITEM
   @Transactional
    public PurchaseOrder addItem(Long poId, PurchaseOrderItemDTO dto) {

        PurchaseOrder po = poRepo.findById(poId)
            .orElseThrow(() -> new RuntimeException("PO not found"));

        BigDecimal base =
            dto.getUnitPrice()
                .multiply(BigDecimal.valueOf(dto.getQuantity()))
                .subtract(dto.getDiscount());

        BigDecimal tax =
            base.multiply(dto.getTaxPercent())
                .divide(BigDecimal.valueOf(100));

        dto.setItemName(base.add(tax));
        dto.setPurchaseOrder(po);

        itemRepo.save(dto);

        // 🔁 Recalculate PO totals
        recalcTotals(po);

        return po;
    }

    /* ================= FULL UPDATE ================= */
    public PurchaseOrder updateItem(Long itemId, PurchaseOrderItem newItem) {

        PurchaseOrderItem item = getItem(itemId);

        item.setItemName(newItem.getItemName());
        item.setQuantity(newItem.getQuantity());
        item.setUnitPrice(newItem.getUnitPrice());
        item.setDiscount(newItem.getDiscount());
        item.setTaxPercent(newItem.getTaxPercent());

        calculateItemTotal(item);
        itemRepo.save(item);

        recalcTotals(item.getPurchaseOrder());
        return item.getPurchaseOrder();
    }

    /* ================= PARTIAL UPDATE ================= */
    public PurchaseOrder patchItem(Long itemId, Map<String, Object> updates) {

        PurchaseOrderItem item = getItem(itemId);

        if (updates.containsKey("quantity"))
            item.setQuantity((Integer) updates.get("quantity"));

        if (updates.containsKey("unitPrice"))
            item.setUnitPrice(new BigDecimal(updates.get("unitPrice").toString()));

        if (updates.containsKey("discount"))
            item.setDiscount(new BigDecimal(updates.get("discount").toString()));

        if (updates.containsKey("taxPercent"))
            item.setTaxPercent(new BigDecimal(updates.get("taxPercent").toString()));

        calculateItemTotal(item);
        itemRepo.save(item);

        recalcTotals(item.getPurchaseOrder());
        return item.getPurchaseOrder();
    }

    /* ================= DELETE ITEM ================= */
    public PurchaseOrder deleteItem(Long itemId) {

        PurchaseOrderItem item = getItem(itemId);
        PurchaseOrder po = item.getPurchaseOrder();

        itemRepo.delete(item);
        recalcTotals(po);

        return po;
    }

    public void deliverItem(Long itemId) {
        PurchaseOrderItem item = itemRepo.findById(itemId).orElseThrow();
        item.setDeliveryStatus("DELIVERED");
        itemRepo.save(item);
    }

    public PurchaseOrder closePO(Long poId) {

        PurchaseOrder po = poRepo.findById(poId)
            .orElseThrow(() -> new RuntimeException("PO not found"));

        if (po.getItems().isEmpty()) {
            throw new RuntimeException("Cannot close PO without items");
        }

        boolean allDelivered = po.getItems()
            .stream()
            .allMatch(i -> i.getDeliveryStatus().equals("DELIVERED"));

        if (!allDelivered) {
            throw new RuntimeException("All items must be delivered");
        }

        po.setPoStatus("CLOSED");
        return poRepo.save(po);
    }

     /* ================= HELPERS ================= */

    private PurchaseOrderItem getItem(Long itemId) {
        return itemRepo.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found"));
    }

    private void calculateItemTotal(PurchaseOrderItem item) {

        BigDecimal base =
                item.getUnitPrice()
                        .multiply(BigDecimal.valueOf(item.getQuantity()))
                        .subtract(item.getDiscount());

        BigDecimal tax =
                base.multiply(item.getTaxPercent())
                        .divide(BigDecimal.valueOf(100));

        item.setItemTotal(base.add(tax));
    }

    private void recalcTotals(PurchaseOrder po) {

        List<PurchaseOrderItem> items = itemRepo.findByPurchaseOrder(po);

        BigDecimal subtotal = BigDecimal.ZERO;
        BigDecimal tax = BigDecimal.ZERO;

        for (PurchaseOrderItem i : items) {
            BigDecimal base =
                    i.getUnitPrice()
                            .multiply(BigDecimal.valueOf(i.getQuantity()))
                            .subtract(i.getDiscount());

            subtotal = subtotal.add(base);
            tax = tax.add(i.getItemTotal().subtract(base));
        }

        po.setSubtotal(subtotal);
        po.setTaxAmount(tax);
        po.setTotalAmount(subtotal.add(tax));
        poRepo.save(po);
    }

}
