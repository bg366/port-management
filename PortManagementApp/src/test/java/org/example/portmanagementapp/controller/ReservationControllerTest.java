package org.example.portmanagementapp.controller;

import org.example.portmanagementapp.dto.ReservationRequest;
import org.example.portmanagementapp.entity.Boat;
import org.example.portmanagementapp.entity.Place;
import org.example.portmanagementapp.entity.Reservation;
import org.example.portmanagementapp.entity.User;
import org.example.portmanagementapp.service.ReservationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReservationControllerTest {

    @Mock
    private ReservationService reservationService;

    @InjectMocks
    private ReservationController reservationController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateReservation() {
        ReservationRequest request = new ReservationRequest();
        request.setUserId(1L);
        request.setBoatId(2L);
        request.setPlaceId(3L);
        request.setStartDate(LocalDate.of(2023, 10, 1));
        request.setEndDate(LocalDate.of(2023, 10, 5));

        User user = new User(1L, "username", "email@example.com", "password", null, "OWNER");
        Boat boat = new Boat(2L, "Boat1", "Sailboat", user, null);
        Place place = new Place(3L, 101, 50.0, true, null);

        Reservation createdReservation = new Reservation(1L, boat, place, user, request.getStartDate(), request.getEndDate());

        when(reservationService.createReservation(request)).thenReturn(createdReservation);

        Reservation response = reservationController.createReservation(request);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Boat1", response.getBoat().getName());
        assertEquals(101, response.getPlace().getNumber());
        verify(reservationService, times(1)).createReservation(request);
    }

    @Test
    void testGetAllReservations() {
        User user = new User(1L, "username", "email@example.com", "password", null, "OWNER");
        Boat boat = new Boat(2L, "Boat1", "Sailboat", user, null);
        Place place = new Place(3L, 101, 50.0, true, null);

        Reservation res1 = new Reservation(1L, boat, place, user, LocalDate.of(2023, 10, 1), LocalDate.of(2023, 10, 5));
        Reservation res2 = new Reservation(2L, boat, place, user, LocalDate.of(2023, 10, 10), LocalDate.of(2023, 10, 15));

        when(reservationService.getAllReservations()).thenReturn(Arrays.asList(res1, res2));

        List<Reservation> reservations = reservationController.getAllReservations();

        assertNotNull(reservations);
        assertEquals(2, reservations.size());
        assertEquals(1L, reservations.get(0).getId());
        assertEquals(2L, reservations.get(1).getId());
        verify(reservationService, times(1)).getAllReservations();
    }

    @Test
    void testGetReservationById_Found() {
        User user = new User(1L, "username", "email@example.com", "password", null, "OWNER");
        Boat boat = new Boat(2L, "Boat1", "Sailboat", user, null);
        Place place = new Place(3L, 101, 50.0, true, null);
        Reservation reservation = new Reservation(1L, boat, place, user, LocalDate.of(2023, 10, 1), LocalDate.of(2023, 10, 5));

        when(reservationService.getReservationById(1L)).thenReturn(Optional.of(reservation));

        ResponseEntity<Reservation> response = reservationController.getReservationById(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
        verify(reservationService, times(1)).getReservationById(1L);
    }

    @Test
    void testGetReservationById_NotFound() {
        when(reservationService.getReservationById(1L)).thenReturn(Optional.empty());

        ResponseEntity<Reservation> response = reservationController.getReservationById(1L);

        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
        verify(reservationService, times(1)).getReservationById(1L);
    }
}