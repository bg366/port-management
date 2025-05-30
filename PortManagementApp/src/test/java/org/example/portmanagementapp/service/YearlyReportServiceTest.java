package org.example.portmanagementapp.service;

import org.example.portmanagementapp.entity.Place;
import org.example.portmanagementapp.entity.Reservation;
import org.example.portmanagementapp.reporting.YearlyReportService;
import org.example.portmanagementapp.repository.ReservationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class YearlyReportServiceTest {

    private ReservationRepository reservationRepository;
    private YearlyReportService yearlyReportService;

    @BeforeEach
    void setUp() {
        reservationRepository = Mockito.mock(ReservationRepository.class);
        yearlyReportService = new YearlyReportService(reservationRepository);
    }

    @Test
    void shouldGenerateSimpleYearlyReport() {
        Place place = new Place();

        Reservation reservation = new Reservation();
        reservation.setStartDate(LocalDate.of(2025, 3, 10));
        reservation.setEndDate(LocalDate.of(2025, 3, 15));
        reservation.setPlace(place);

        Mockito.when(reservationRepository.findAllByStartDateBetween(
                LocalDate.of(2025, 1, 1),
                LocalDate.of(2025, 12, 31)
        )).thenReturn(List.of(reservation));

        String report = yearlyReportService.generateReport(0, 2025);

        assertThat(report).contains("Raport roczny za rok: 2025");
        assertThat(report).contains("Liczba rezerwacji: 1");
        assertThat(report).contains("Łączna suma opłat: 500.0 PLN");
    }

    @Test
    void shouldGenerateYearlyReportWithMultipleReservations() {
        Place place1 = new Place();
        Place place2 = new Place();

        Reservation reservation1 = new Reservation();
        reservation1.setStartDate(LocalDate.of(2025, 4, 5));
        reservation1.setEndDate(LocalDate.of(2025, 4, 10));
        reservation1.setPlace(place1);

        Reservation reservation2 = new Reservation();
        reservation2.setStartDate(LocalDate.of(2025, 8, 20));
        reservation2.setEndDate(LocalDate.of(2025, 8, 22));
        reservation2.setPlace(place2);

        Mockito.when(reservationRepository.findAllByStartDateBetween(
                LocalDate.of(2025, 1, 1),
                LocalDate.of(2025, 12, 31)
        )).thenReturn(List.of(reservation1, reservation2));

        String report = yearlyReportService.generateReport(0, 2025);

        assertThat(report).contains("Raport roczny za rok: 2025");
        assertThat(report).contains("Liczba rezerwacji: 2");
        assertThat(report).contains("Łączna suma opłat: 1000.0 PLN");
    }

    @Test
    void shouldGenerateEmptyYearlyReport() {
        Mockito.when(reservationRepository.findAllByStartDateBetween(
                LocalDate.of(2025, 1, 1),
                LocalDate.of(2025, 12, 31)
        )).thenReturn(List.of());

        String report = yearlyReportService.generateReport(0, 2025);

        assertThat(report).contains("Raport roczny za rok: 2025");
        assertThat(report).contains("Liczba rezerwacji: 0");
        assertThat(report).contains("Łączna suma opłat: 0.0 PLN");
    }
}