package org.example.portmanagementapp.invoice;

import org.example.portmanagementapp.entity.Boat;
import org.example.portmanagementapp.entity.Reservation;
import org.example.portmanagementapp.entity.User;
import org.example.portmanagementapp.repository.ReservationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class BasicInvoiceGeneratorTest {

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private BasicInvoiceGenerator basicInvoiceGenerator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGenerateInvoices_ForGivenMonth() {
        YearMonth month = YearMonth.of(2023, 10);

        User user1 = new User(1L, "user1", "user1@example.com", "password1", null, "OWNER");
        User user2 = new User(2L, "user2", "user2@example.com", "password2", null, "OWNER");

        Boat boat1 = new Boat(1L, "Boat1", "Motorboat", user1, null);
        Boat boat2 = new Boat(2L, "Boat2", "Sailboat", user2, null);

        Reservation reservation1 = new Reservation(1L, boat1, null, user1, LocalDate.of(2023, 10, 1), LocalDate.of(2023, 10, 5));
        Reservation reservation2 = new Reservation(2L, boat2, null, user2, LocalDate.of(2023, 10, 10), LocalDate.of(2023, 10, 13));

        when(reservationRepository.findAll()).thenReturn(List.of(reservation1, reservation2));

        List<Invoice> invoices = basicInvoiceGenerator.generateInvoices(month);

        assertEquals(2, invoices.size());

        Invoice user1Invoice = invoices.stream().filter(i -> i.getUsername().equals("user1")).findFirst().orElse(null);
        Invoice user2Invoice = invoices.stream().filter(i -> i.getUsername().equals("user2")).findFirst().orElse(null);

        assertEquals(400.0, user1Invoice.getTotalAmount());
        assertEquals(300.0, user2Invoice.getTotalAmount());
        assertEquals(month, user1Invoice.getMonth());
        assertEquals(month, user2Invoice.getMonth());

        verify(reservationRepository, times(1)).findAll();
    }

    @Test
    void testGenerateInvoices_NoReservationsInMonth() {
        YearMonth month = YearMonth.of(2023, 11);

        User user = new User(1L, "user1", "user1@example.com", "password1", null, "OWNER");
        Boat boat = new Boat(1L, "Boat1", "Sailboat", user, null);

        Reservation reservation = new Reservation(1L, boat, null, user, LocalDate.of(2023, 10, 1), LocalDate.of(2023, 10, 5));

        when(reservationRepository.findAll()).thenReturn(List.of(reservation));

        List<Invoice> invoices = basicInvoiceGenerator.generateInvoices(month);

        assertEquals(0, invoices.size());
        verify(reservationRepository, times(1)).findAll();
    }

    @Test
    void testGenerateInvoices_NoReservationsAtAll() {
        YearMonth month = YearMonth.of(2023, 10);

        when(reservationRepository.findAll()).thenReturn(Collections.emptyList());

        List<Invoice> invoices = basicInvoiceGenerator.generateInvoices(month);

        assertEquals(0, invoices.size());
        verify(reservationRepository, times(1)).findAll();
    }

    @Test
    void testGenerateInvoices_MultipleReservationsForSameUser() {
        YearMonth month = YearMonth.of(2023, 10);

        User user = new User(1L, "user1", "user1@example.com", "password1", null, "OWNER");

        Boat boat1 = new Boat(1L, "Boat1", "Motorboat", user, null);
        Boat boat2 = new Boat(2L, "Boat2", "Sailboat", user, null);

        Reservation reservation1 = new Reservation(1L, boat1, null, user, LocalDate.of(2023, 10, 1), LocalDate.of(2023, 10, 5));
        Reservation reservation2 = new Reservation(2L, boat2, null, user, LocalDate.of(2023, 10, 10), LocalDate.of(2023, 10, 12));

        when(reservationRepository.findAll()).thenReturn(List.of(reservation1, reservation2));

        List<Invoice> invoices = basicInvoiceGenerator.generateInvoices(month);

        assertEquals(1, invoices.size());

        Invoice invoice = invoices.get(0);

        assertEquals("user1", invoice.getUsername());
        assertEquals(600.0, invoice.getTotalAmount());
        assertEquals(month, invoice.getMonth());

        verify(reservationRepository, times(1)).findAll();
    }
}