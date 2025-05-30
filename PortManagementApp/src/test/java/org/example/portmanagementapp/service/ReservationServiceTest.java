package org.example.portmanagementapp.service;

import org.example.portmanagementapp.dto.ReservationRequest;
import org.example.portmanagementapp.entity.*;
import org.example.portmanagementapp.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReservationServiceTest {

    private ReservationRepository reservationRepository;
    private UserRepository userRepository;
    private BoatRepository boatRepository;
    private PlaceRepository placeRepository;
    private ReservationService reservationService;

    @BeforeEach
    void setUp() {
        reservationRepository = mock(ReservationRepository.class);
        userRepository = mock(UserRepository.class);
        boatRepository = mock(BoatRepository.class);
        placeRepository = mock(PlaceRepository.class);
        reservationService = new ReservationService(reservationRepository, userRepository, boatRepository, placeRepository);
    }

    @Test
    void shouldCreateReservationSuccessfully() {
        User user = new User();
        user.setId(1L);

        Boat boat = new Boat();
        boat.setId(2L);
        boat.setOwner(user);

        Place place = new Place();
        place.setId(3L);

        ReservationRequest request = new ReservationRequest();
        request.userId = 1L;
        request.boatId = 2L;
        request.placeId = 3L;
        request.startDate = LocalDate.now();
        request.endDate = LocalDate.now().plusDays(2);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(boatRepository.findById(2L)).thenReturn(Optional.of(boat));
        when(placeRepository.findById(3L)).thenReturn(Optional.of(place));
        when(reservationRepository.existsByPlaceAndDates(place, request.startDate, request.endDate)).thenReturn(false);
        when(reservationRepository.save(any(Reservation.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Reservation reservation = reservationService.createReservation(request);

        assertNotNull(reservation);
        assertEquals(user, reservation.getUser());
        assertEquals(boat, reservation.getBoat());
        assertEquals(place, reservation.getPlace());
        assertEquals(request.startDate, reservation.getStartDate());
        assertEquals(request.endDate, reservation.getEndDate());
    }

    @Test
    void shouldThrowExceptionWhenPlaceIsTaken() {
        User user = new User();
        user.setId(1L);

        Boat boat = new Boat();
        boat.setId(2L);
        boat.setOwner(user);

        Place place = new Place();
        place.setId(3L);

        ReservationRequest request = new ReservationRequest();
        request.userId = 1L;
        request.boatId = 2L;
        request.placeId = 3L;
        request.startDate = LocalDate.now();
        request.endDate = LocalDate.now().plusDays(2);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(boatRepository.findById(2L)).thenReturn(Optional.of(boat));
        when(placeRepository.findById(3L)).thenReturn(Optional.of(place));
        when(reservationRepository.existsByPlaceAndDates(place, request.startDate, request.endDate)).thenReturn(true);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            reservationService.createReservation(request);
        });

        assertTrue(ex.getMessage().contains("Place already reserved"));
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        ReservationRequest request = new ReservationRequest();
        request.userId = 999L;
        request.boatId = 1L;
        request.placeId = 1L;
        request.startDate = LocalDate.now();
        request.endDate = LocalDate.now().plusDays(2);

        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            reservationService.createReservation(request);
        });

        assertTrue(ex.getMessage().contains("User not found"));
    }

    @Test
    void shouldThrowExceptionWhenPlaceNotFound() {
        User user = new User();
        user.setId(1L);

        Boat boat = new Boat();
        boat.setId(2L);
        boat.setOwner(user);

        ReservationRequest request = new ReservationRequest();
        request.userId = 1L;
        request.boatId = 2L;
        request.placeId = 999L;
        request.startDate = LocalDate.now();
        request.endDate = LocalDate.now().plusDays(2);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(boatRepository.findById(2L)).thenReturn(Optional.of(boat));
        when(placeRepository.findById(999L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            reservationService.createReservation(request);
        });

        assertTrue(ex.getMessage().contains("Place not found"));
    }
}
