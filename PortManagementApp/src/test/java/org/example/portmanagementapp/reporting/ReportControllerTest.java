package org.example.portmanagementapp.reporting;

import org.example.portmanagementapp.dto.DockReportDto;
import org.example.portmanagementapp.invoice.Invoice;
import org.example.portmanagementapp.invoice.InvoiceService;
import org.example.portmanagementapp.service.DockReportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReportControllerTest {

    @Mock
    private MonthlyReportService reportService;

    @Mock
    private DockReportService dockReportService;

    @Mock
    private InvoiceService invoiceService;

    @InjectMocks
    private ReportController reportController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void generateMonthlyReport() {
        when(reportService.generateReport(1, 2023)).thenReturn("Monthly Report");

        String result = reportController.generateMonthlyReport(1, 2023);

        assertEquals("Monthly Report", result);
        verify(reportService, times(1)).generateReport(1, 2023);
    }

    @Test
    void getDockReport() {
        DockReportDto dto = new DockReportDto();
        dto.totalPlaces = 10;
        dto.occupiedPlaces = 5;
        dto.freePlaces = 5;
        dto.estimatedRevenue = 1000.0;
        dto.estimatedMediaCost = 150.0;

        when(dockReportService.generateDockReport(LocalDate.of(2023, 10, 1))).thenReturn(dto);

        DockReportDto result = reportController.getDockReport(LocalDate.of(2023, 10, 1));

        assertEquals(10, result.totalPlaces);
        assertEquals(5, result.occupiedPlaces);
        assertEquals(1000.0, result.estimatedRevenue);
        verify(dockReportService, times(1)).generateDockReport(LocalDate.of(2023, 10, 1));
    }

    @Test
    void getInvoices() {
        Invoice invoice1 = new Invoice("user1", 200.0, YearMonth.of(2023, 10));
        Invoice invoice2 = new Invoice("user2", 300.0, YearMonth.of(2023, 10));

        when(invoiceService.generateForMonth(YearMonth.of(2023, 10))).thenReturn(Arrays.asList(invoice1, invoice2));

        List<Invoice> result = reportController.getInvoices(2023, 10);

        assertEquals(2, result.size());
        assertEquals("user1", result.get(0).getUsername());
        assertEquals("user2", result.get(1).getUsername());
        verify(invoiceService, times(1)).generateForMonth(YearMonth.of(2023, 10));
    }
}