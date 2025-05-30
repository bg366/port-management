package org.example.portmanagementapp.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "reservations")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "boat_id", nullable = false)
    @JsonBackReference
    private Boat boat;

    @ManyToOne
    @JoinColumn(name = "place_id", nullable = false)
    @JsonBackReference
    private Place place;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    private LocalDate startDate;
    private LocalDate endDate;

    public double getTotalPrice() {
        if (startDate == null || endDate == null || !endDate.isAfter(startDate)) {
            return 0;
        }
        long numOfDays = ChronoUnit.DAYS.between(startDate, endDate);
        return numOfDays * 100;
    }
}
