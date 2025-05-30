package org.example.portmanagementapp.service;

import org.example.portmanagementapp.entity.Boat;
import org.example.portmanagementapp.entity.Reservation;
import org.example.portmanagementapp.entity.User;
import org.example.portmanagementapp.invoice.BasicInvoiceGenerator;
import org.example.portmanagementapp.invoice.Invoice;
import org.example.portmanagementapp.invoice.InvoiceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class InvoiceServiceTest {

    @Mock
    private BasicInvoiceGenerator generator;

    @InjectMocks
    private InvoiceService invoiceService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGenerateForMonth_WithInvoices() {
        YearMonth month = YearMonth.of(2023, 10);
        Invoice invoice1 = new Invoice("user1", 500.0, month);
        Invoice invoice2 = new Invoice("user2", 300.0, month);

        when(generator.generateInvoices(month)).thenReturn(List.of(invoice1, invoice2));

        List<Invoice> invoices = invoiceService.generateForMonth(month);

        assertEquals(2, invoices.size());
        assertEquals(invoice1, invoices.get(0));
        assertEquals(invoice2, invoices.get(1));
        verify(generator, times(1)).generateInvoices(month);
    }

    @Test
    void testGenerateForMonth_NoInvoices() {
        YearMonth month = YearMonth.of(2023, 11);

        when(generator.generateInvoices(month)).thenReturn(List.of());

        List<Invoice> invoices = invoiceService.generateForMonth(month);

        assertEquals(0, invoices.size());
        verify(generator, times(1)).generateInvoices(month);
    }

    @Test
    void testGenerateForMonth_VerifyTotalAmountCalculation() {
        YearMonth month = YearMonth.of(2023, 10);

        User user = new User(1L, "user1", "user1@example.com", "password", null, "OWNER");
        Boat boat = new Boat(1L, "Boat1", "Sailboat", user, null);

        Reservation reservation1 = new Reservation(1L, boat, null, user, LocalDate.of(2023, 10, 1), LocalDate.of(2023, 10, 5));
        Reservation reservation2 = new Reservation(2L, boat, null, user, LocalDate.of(2023, 10, 10), LocalDate.of(2023, 10, 15));

        double expectedTotalAmount = reservation1.getTotalPrice() + reservation2.getTotalPrice();

        Invoice invoice = new Invoice(user.getUsername(), expectedTotalAmount, month);
        when(generator.generateInvoices(month)).thenReturn(List.of(invoice));

        List<Invoice> invoices = invoiceService.generateForMonth(month);

        assertEquals(1, invoices.size());
        Invoice generatedInvoice = invoices.get(0);
        assertEquals(user.getUsername(), generatedInvoice.getUsername());
        assertEquals(expectedTotalAmount, generatedInvoice.getTotalAmount());
        assertEquals(month, generatedInvoice.getMonth());
    }
}