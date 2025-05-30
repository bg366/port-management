package org.example.portmanagementapp.reporting;

import org.example.portmanagementapp.entity.Reservation;
import org.example.portmanagementapp.repository.ReservationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class YearlyReportService implements ReportGenerator {

    private final ReservationRepository reservationRepository;

    public YearlyReportService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @Override
    public String generateReport(int month, int year) {
        LocalDate start = LocalDate.of(year, 1, 1);
        LocalDate end = start.plusYears(1).minusDays(1);

        List<Reservation> reservations = reservationRepository.findAllByStartDateBetween(start, end);

        double totalRevenue = reservations.stream()
                .mapToDouble(Reservation::getTotalPrice)
                .sum();

        return "Raport roczny za rok: " + year +
                "\nLiczba rezerwacji: " + reservations.size() +
                "\nŁączna suma opłat: " + totalRevenue + " PLN";
    }
}