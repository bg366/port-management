package org.example.portmanagementapp.reporting;

import org.example.portmanagementapp.dto.DockReportDto;
import org.example.portmanagementapp.invoice.InvoiceService;
import org.example.portmanagementapp.service.DockReportService;
import org.example.portmanagementapp.invoice.Invoice;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@RestController
@RequestMapping("/admin/report")
public class ReportController {

    private final MonthlyReportService reportService;
    private final DockReportService dockReportService;
    private final InvoiceService invoiceService;
    private final YearlyReportService yearlyReportService;

    public ReportController(MonthlyReportService reportService, DockReportService dockReportService, InvoiceService invoiceService, YearlyReportService yearlyReportService) {
        this.reportService = reportService;
        this.dockReportService = dockReportService;
        this.invoiceService = invoiceService;
        this.yearlyReportService = yearlyReportService;
    }

    @GetMapping("/monthly")
    public String generateMonthlyReport(@RequestParam int month, @RequestParam int year) {
        return reportService.generateReport(month, year);
    }

    @GetMapping("/yearly")
    public String generateYearlyReport(@RequestParam int year) {
        return yearlyReportService.generateReport(0, year);
    }

    @GetMapping("/dock")
    public DockReportDto getDockReport(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return dockReportService.generateDockReport(date);
    }

    @GetMapping("/reports/invoices")
    public List<Invoice> getInvoices(@RequestParam int year, @RequestParam int month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        return invoiceService.generateForMonth(yearMonth);
    }
}
