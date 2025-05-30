package org.example.portmanagementapp.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class ReservationRequest {
    public Long userId;
    public Long boatId;
    public Long placeId;
    public LocalDate startDate;
    public LocalDate endDate;
}
