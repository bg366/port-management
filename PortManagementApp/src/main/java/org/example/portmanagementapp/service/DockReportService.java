package org.example.portmanagementapp.service;

import lombok.RequiredArgsConstructor;
import org.example.portmanagementapp.dto.DockReportDto;
import org.example.portmanagementapp.entity.Place;
import org.example.portmanagementapp.entity.Reservation;
import org.example.portmanagementapp.repository.PlaceRepository;
import org.example.portmanagementapp.repository.ReservationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DockReportService {

    private final PlaceRepository placeRepository;
    private final ReservationRepository reservationRepository;

    public DockReportDto generateDockReport(LocalDate date) {
        List<Place> allPlaces = placeRepository.findAll();
        List<Reservation> reservations = reservationRepository.findAllByStartDateBetween(date, date);

        int total = allPlaces.size();
        int occupied = (int) reservations.stream()
                .map(Reservation::getPlace)
                .distinct()
                .count();

        double revenue = reservations.stream()
                .mapToDouble(Reservation::getTotalPrice)
                .sum();

        DockReportDto dto = new DockReportDto();
        dto.totalPlaces = total;
        dto.occupiedPlaces = occupied;
        dto.freePlaces = total - occupied;
        dto.estimatedRevenue = revenue;
        dto.estimatedMediaCost = occupied * 30.0;

        return dto;
    }
}
