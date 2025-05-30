package org.example.portmanagementapp.invoice;

import lombok.RequiredArgsConstructor;
import org.example.portmanagementapp.entity.Reservation;
import org.example.portmanagementapp.entity.User;
import org.example.portmanagementapp.repository.ReservationRepository;
import org.springframework.stereotype.Component;

import java.time.YearMonth;
import java.util.*;

@Component
@RequiredArgsConstructor
public class BasicInvoiceGenerator implements InvoiceGenerator {

    private final ReservationRepository reservationRepository;

    @Override
    public List<Invoice> generateInvoices(YearMonth month) {
        List<Reservation> reservations = reservationRepository.findAll();

        Map<User, Double> userToTotal = new HashMap<>();

        for (Reservation res : reservations) {
            if (res.getStartDate().getYear() == month.getYear() &&
                    res.getStartDate().getMonth() == month.getMonth()) {

                User user = res.getBoat().getOwner();
                double amount = res.getTotalPrice();
                userToTotal.merge(user, amount, Double::sum);
            }
        }

        List<Invoice> invoices = new ArrayList<>();
        for (Map.Entry<User, Double> entry : userToTotal.entrySet()) {
            invoices.add(new Invoice(entry.getKey().getUsername(), entry.getValue(), month));
        }

        return invoices;
    }
}
