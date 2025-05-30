package org.example.portmanagementapp.controller;

import org.example.portmanagementapp.entity.Boat;
import org.example.portmanagementapp.entity.User;
import org.example.portmanagementapp.service.BoatService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class BoatControllerTest {

    @Mock
    private BoatService boatService;

    @InjectMocks
    private BoatController boatController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateBoat() {
        User user = new User(1L, "ownerUsername", "owner@email.com", "password", null, "OWNER");
        Boat boat = new Boat(null, "Boat Name", "Sailboat", user, null);
        Boat savedBoat = new Boat(1L, "Boat Name", "Sailboat", user, null);

        when(boatService.createBoat(boat)).thenReturn(savedBoat);

        ResponseEntity<Boat> response = boatController.createBoat(boat);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals(savedBoat, response.getBody());
        verify(boatService, times(1)).createBoat(boat);
    }

    @Test
    void testGetAllBoats() {
        User user = new User(1L, "ownerUsername", "owner@email.com", "password", null, "OWNER");
        List<Boat> boats = List.of(
                new Boat(1L, "Boat 1", "Motorboat", user, null),
                new Boat(2L, "Boat 2", "Sailboat", user, null)
        );

        when(boatService.getAllBoats()).thenReturn(boats);

        List<Boat> result = boatController.getAllBoats();

        assertEquals(boats.size(), result.size());
        assertEquals(boats, result);
        verify(boatService, times(1)).getAllBoats();
    }

    @Test
    void testGetBoatById_Found() {
        User user = new User(1L, "ownerUsername", "owner@email.com", "password", null, "OWNER");
        Boat boat = new Boat(1L, "Boat Name", "Motorboat", user, null);

        when(boatService.getBoatById(1L)).thenReturn(Optional.of(boat));

        ResponseEntity<Boat> response = boatController.getBoatById(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(boat, response.getBody());
        verify(boatService, times(1)).getBoatById(1L);
    }

    @Test
    void testGetBoatById_NotFound() {
        when(boatService.getBoatById(1L)).thenReturn(Optional.empty());

        ResponseEntity<Boat> response = boatController.getBoatById(1L);

        assertEquals(404, response.getStatusCodeValue());
        verify(boatService, times(1)).getBoatById(1L);
    }
}