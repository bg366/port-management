package org.example.portmanagementapp.reporting;

import org.example.portmanagementapp.entity.Reservation;
import org.example.portmanagementapp.repository.ReservationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class MonthlyReportService implements ReportGenerator {

    private final ReservationRepository reservationRepository;

    public MonthlyReportService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @Override
    public String generateReport(int month, int year) {
        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.plusMonths(1).minusDays(1);

        List<Reservation> reservations = reservationRepository.findAllByStartDateBetween(start, end);

        double total = reservations.stream()
                .mapToDouble(Reservation::getTotalPrice)
                .sum();

        return "Raport za: " + month + "/" + year +
                "\nLiczba rezerwacji: " + reservations.size() +
                "\nSuma opłat: " + total + " PLN";
    }
}
