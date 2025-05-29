package org.example.portmanagementapp.controller;

import org.example.portmanagementapp.dto.DockReportDto;
import org.example.portmanagementapp.reporting.DockReportService;
import org.example.portmanagementapp.reporting.MonthlyReportService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/report")
public class ReportController {

    private final MonthlyReportService reportService;
    private final DockReportService dockReportService;

    public ReportController(MonthlyReportService reportService, DockReportService dockReportService) {
        this.reportService = reportService;
        this.dockReportService = dockReportService;
    }

    @GetMapping("/monthly")
    public String generateMonthlyReport(@RequestParam int month, @RequestParam int year) {
        return reportService.generateReport(month, year);
    }

    @GetMapping("/dock")
    public DockReportDto getDockReport(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return dockReportService.generateDockReport(date);
    }
}
