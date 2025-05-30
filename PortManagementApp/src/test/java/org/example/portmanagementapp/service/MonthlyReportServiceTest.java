package org.example.portmanagementapp.service;

import org.example.portmanagementapp.entity.Place;
import org.example.portmanagementapp.entity.Reservation;
import org.example.portmanagementapp.repository.ReservationRepository;
import org.example.portmanagementapp.reporting.MonthlyReportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class MonthlyReportServiceTest {

    private ReservationRepository reservationRepository;
    private MonthlyReportService monthlyReportService;

    @BeforeEach
    void setUp() {
        reservationRepository = Mockito.mock(ReservationRepository.class);
        monthlyReportService = new MonthlyReportService(reservationRepository);
    }

    @Test
    void shouldGenerateSimpleMonthlyReport() {
        Place place = new Place();

        Reservation reservation = new Reservation();
        reservation.setStartDate(LocalDate.of(2025, 5, 10));
        reservation.setEndDate(LocalDate.of(2025, 5, 12));
        reservation.setPlace(place);

        Mockito.when(reservationRepository.findAllByStartDateBetween(
                LocalDate.of(2025, 5, 1),
                LocalDate.of(2025, 5, 31)
        )).thenReturn(List.of(reservation));

        String report = monthlyReportService.generateReport(5, 2025);

        assertThat(report).contains("Raport za: 5/2025");
        assertThat(report).contains("Liczba rezerwacji: 1");
        assertThat(report).contains("Suma opłat: 200.0 PLN");
    }
}
