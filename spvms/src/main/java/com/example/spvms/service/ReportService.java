package com.example.spvms.service;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.*;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;

@Service
public class ReportService {

    public byte[] generateReport(String reportName,
                                 Collection<?> data,
                                 String format) throws Exception {

        InputStream reportStream =
                getClass().getResourceAsStream("/reports/" + reportName + ".jrxml");

        JasperReport jasperReport =
                JasperCompileManager.compileReport(reportStream);

        JRBeanCollectionDataSource dataSource =
                new JRBeanCollectionDataSource(data);

        JasperPrint jasperPrint =
                JasperFillManager.fillReport(
                        jasperReport,
                        new HashMap<>(),
                        dataSource
                );

        // PDF
        if ("pdf".equalsIgnoreCase(format)) {
            return JasperExportManager.exportReportToPdf(jasperPrint);
        }

        // EXCEL
        if ("excel".equalsIgnoreCase(format)) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            JRXlsxExporter exporter = new JRXlsxExporter();

            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(out));
            exporter.exportReport();

            return out.toByteArray();
        }

        throw new IllegalArgumentException("Invalid format");
    }
}
