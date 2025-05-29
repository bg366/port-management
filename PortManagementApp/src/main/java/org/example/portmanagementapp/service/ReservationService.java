package org.example.portmanagementapp.service;

import org.example.portmanagementapp.dto.ReservationRequest;
import org.example.portmanagementapp.entity.Boat;
import org.example.portmanagementapp.entity.Place;
import org.example.portmanagementapp.entity.Reservation;
import org.example.portmanagementapp.entity.User;
import org.example.portmanagementapp.repository.BoatRepository;
import org.example.portmanagementapp.repository.PlaceRepository;
import org.example.portmanagementapp.repository.ReservationRepository;
import org.example.portmanagementapp.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final BoatRepository boatRepository;
    private final PlaceRepository placeRepository;

    public ReservationService(ReservationRepository reservationRepository, UserRepository userRepository, BoatRepository boatRepository, PlaceRepository placeRepository) {
        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
        this.boatRepository = boatRepository;
        this.placeRepository = placeRepository;
    }

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    public Optional<Reservation> getReservationById(Long id) {
        return reservationRepository.findById(id);
    }

    public Reservation createReservation(ReservationRequest request) {
        User user = userRepository.findById(request.userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Boat boat = boatRepository.findById(request.boatId)
                .orElseThrow(() -> new RuntimeException("Boat not found"));
        Place place = placeRepository.findById(request.placeId)
                .orElseThrow(() -> new RuntimeException("Place not found"));

        boolean isTaken = reservationRepository.existsByPlaceAndDates(place, request.startDate, request.endDate);
        if (isTaken) {
            throw new RuntimeException("Place already reserved for that period");
        }

        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setBoat(boat);
        reservation.setPlace(place);
        reservation.setStartDate(request.startDate);
        reservation.setEndDate(request.endDate);

        return reservationRepository.save(reservation);
    }


    public void deleteReservation(Long id) {
        reservationRepository.deleteById(id);
    }
}
