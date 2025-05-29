package org.example.portmanagementapp.repository;

import org.example.portmanagementapp.entity.Place;
import org.example.portmanagementapp.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    @Query("""
        SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END
        FROM Reservation r
        WHERE r.place = :place
          AND r.startDate < :endDate
          AND r.endDate > :startDate
    """)
    boolean existsByPlaceAndDates(
            @Param("place") Place place,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    List<Reservation> findAllByStartDateBetween(LocalDate start, LocalDate end);
}
