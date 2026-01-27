package com.example.spvms.controllers;

import com.example.spvms.model.PurchaseOrder;
import com.example.spvms.model.PurchaseRequisition;
import com.example.spvms.model.Vendor;
import com.example.spvms.repository.PurchaseOrderRepository;
import com.example.spvms.service.PurchaseRequisitionService;
import com.example.spvms.service.ReportService;
import com.example.spvms.service.VendorService;

import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reports")
public class ReportController {

    private final VendorService vendorService;
    private final PurchaseRequisitionService prService;
    private final PurchaseOrderRepository poRepository;
    private final ReportService reportService;

    public ReportController(VendorService vendorService,
                            PurchaseRequisitionService prService,
                            PurchaseOrderRepository poRepository,
                            ReportService reportService) {
        this.vendorService = vendorService;
        this.prService = prService;
        this.poRepository = poRepository;
        this.reportService = reportService;
    }

    // ================= VENDOR REPORT =================
    @GetMapping("/vendor")
    public ResponseEntity<byte[]> vendorReport(@RequestParam String format) throws Exception {

        validateFormat(format);

        List<Vendor> vendors = vendorService.getAllVendors();
        byte[] data = reportService.generateReport("vendor_report", vendors, format);

        return buildResponse(data, format, "Vendor_Report");
    }

    // ================= PR REPORT =================
    @GetMapping("/pr")
    public ResponseEntity<byte[]> prReport(@RequestParam String format) throws Exception {

        validateFormat(format);

        List<PurchaseRequisition> prs = prService.getAll();
        byte[] data = reportService.generateReport("pr_report", prs, format);

        return buildResponse(data, format, "PR_Report");
    }

    // ================= PO REPORT =================
    @GetMapping("/po")
    public ResponseEntity<byte[]> poReport(@RequestParam String format) throws Exception {

        validateFormat(format);

        List<PurchaseOrder> pos = poRepository.findAll();
        byte[] data = reportService.generateReport("po_report", pos, format);

        return buildResponse(data, format, "PO_Report");
    }

    // ================= FORMAT VALIDATION =================
    private void validateFormat(String format) {
        if (!"pdf".equalsIgnoreCase(format) &&
            !"excel".equalsIgnoreCase(format)) {

            throw new IllegalArgumentException(
                "Invalid format. Supported formats are: pdf, excel"
            );
        }
    }

    // ================= RESPONSE BUILDER =================
    private ResponseEntity<byte[]> buildResponse(byte[] data,
                                                 String format,
                                                 String fileName) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentLength(data.length);

        if ("pdf".equalsIgnoreCase(format)) {

            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDisposition(
                ContentDisposition.attachment()
                    .filename(fileName + ".pdf")
                    .build()
            );

        } else {

            headers.setContentType(
                MediaType.parseMediaType(
                    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                )
            );
            headers.setContentDisposition(
                ContentDisposition.attachment()
                    .filename(fileName + ".xlsx")
                    .build()
            );
        }

        return ResponseEntity.ok()
                .headers(headers)
                .body(data);
    }
}
