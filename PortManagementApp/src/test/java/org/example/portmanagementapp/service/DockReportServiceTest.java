package org.example.portmanagementapp.service;

import org.example.portmanagementapp.dto.DockReportDto;
import org.example.portmanagementapp.entity.Place;
import org.example.portmanagementapp.entity.Reservation;
import org.example.portmanagementapp.repository.PlaceRepository;
import org.example.portmanagementapp.repository.ReservationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

class DockReportServiceTest {

    @Mock
    private PlaceRepository placeRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private DockReportService dockReportService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGenerateDockReport_NoPlacesOrReservations() {
        when(placeRepository.findAll()).thenReturn(Collections.emptyList());
        when(reservationRepository.findAllByStartDateBetween(any(), any())).thenReturn(Collections.emptyList());

        DockReportDto report = dockReportService.generateDockReport(LocalDate.now());

        assertEquals(0, report.totalPlaces);
        assertEquals(0, report.occupiedPlaces);
        assertEquals(0, report.freePlaces);
        assertEquals(0, report.estimatedRevenue, 0.01);
    }

    @Test
    void testGenerateDockReport_AllPlacesOccupied() {
        Place place1 = new Place(1L, 1, 30.0, false, null);
        Place place2 = new Place(2L, 2, 40.0, false, null);

        Reservation reservation1 = createReservation(place1, LocalDate.of(2023, 10, 1), LocalDate.of(2023, 10, 5));
        Reservation reservation2 = createReservation(place2, LocalDate.of(2023, 10, 2), LocalDate.of(2023, 10, 6));

        when(placeRepository.findAll()).thenReturn(List.of(place1, place2));
        when(reservationRepository.findAllByStartDateBetween(any(), any())).thenReturn(List.of(reservation1, reservation2));

        DockReportDto report = dockReportService.generateDockReport(LocalDate.now());

        double expectedRevenue = 4 * 100 + 4 * 100;

        assertEquals(2, report.totalPlaces);
        assertEquals(2, report.occupiedPlaces);
        assertEquals(0, report.freePlaces);
        assertEquals(expectedRevenue, report.estimatedRevenue, 0.01);
    }

    @Test
    void testGenerateDockReport_SomeFreePlaces() {
        Place occupiedPlace = new Place(1L, 1, 30.0, false, null);
        Place freePlace = new Place(2L, 2, 40.0, true, null);

        Reservation reservation = createReservation(occupiedPlace, LocalDate.of(2023, 10, 10), LocalDate.of(2023, 10, 15));

        when(placeRepository.findAll()).thenReturn(List.of(occupiedPlace, freePlace));
        when(reservationRepository.findAllByStartDateBetween(any(), any())).thenReturn(List.of(reservation));

        DockReportDto report = dockReportService.generateDockReport(LocalDate.now());

        double expectedRevenue = 5 * 100;

        assertEquals(2, report.totalPlaces);
        assertEquals(1, report.occupiedPlaces);
        assertEquals(1, report.freePlaces);
        assertEquals(expectedRevenue, report.estimatedRevenue, 0.01);
    }

    @Test
    void testGenerateDockReport_NoReservations() {
        Place place1 = new Place(1L, 1, 30.0, true, null);
        Place place2 = new Place(2L, 2, 40.0, true, null);

        when(placeRepository.findAll()).thenReturn(List.of(place1, place2));
        when(reservationRepository.findAllByStartDateBetween(any(), any())).thenReturn(Collections.emptyList());

        DockReportDto report = dockReportService.generateDockReport(LocalDate.now());

        assertEquals(2, report.totalPlaces);
        assertEquals(0, report.occupiedPlaces);
        assertEquals(2, report.freePlaces);
        assertEquals(0, report.estimatedRevenue, 0.01);
    }

    private Reservation createReservation(Place place, LocalDate startDate, LocalDate endDate) {
        Reservation reservation = new Reservation();
        reservation.setPlace(place);
        reservation.setStartDate(startDate);
        reservation.setEndDate(endDate);

        return reservation;
    }
}