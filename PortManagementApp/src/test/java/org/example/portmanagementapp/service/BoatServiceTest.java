package org.example.portmanagementapp.service;

import org.example.portmanagementapp.entity.Boat;
import org.example.portmanagementapp.entity.User;
import org.example.portmanagementapp.repository.BoatRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BoatServiceTest {

    private BoatRepository boatRepository;
    private BoatService boatService;

    @BeforeEach
    void setUp() {
        boatRepository = mock(BoatRepository.class);
        boatService = new BoatService(boatRepository);
    }

    @Test
    void shouldReturnAllBoats() {
        User user = new User(1L, "username", "email@example.com", "password", null, "OWNER");

        Boat boat1 = new Boat(1L, "Boat1", "Sailboat", user, null);
        Boat boat2 = new Boat(2L, "Boat2", "Motorboat", user, null);

        when(boatRepository.findAll()).thenReturn(Arrays.asList(boat1, boat2));

        List<Boat> boats = boatService.getAllBoats();

        assertEquals(2, boats.size());
        assertEquals("Boat1", boats.get(0).getName());
        assertEquals("Boat2", boats.get(1).getName());
        verify(boatRepository, times(1)).findAll();
    }

    @Test
    void shouldReturnBoatById_WhenFound() {
        User user = new User(1L, "username", "email@example.com", "password", null, "OWNER");

        Boat boat = new Boat(1L, "Boat1", "Sailboat", user, null);

        when(boatRepository.findById(1L)).thenReturn(Optional.of(boat));

        Optional<Boat> result = boatService.getBoatById(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        assertEquals("Boat1", result.get().getName());
        verify(boatRepository, times(1)).findById(1L);
    }

    @Test
    void shouldReturnEmpty_WhenBoatByIdNotFound() {
        when(boatRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Boat> result = boatService.getBoatById(1L);

        assertFalse(result.isPresent());
        verify(boatRepository, times(1)).findById(1L);
    }

    @Test
    void shouldCreateNewBoat() {
        User user = new User(1L, "username", "email@example.com", "password", null, "OWNER");

        Boat boat = new Boat(null, "Boat1", "Sailboat", user, null);
        Boat savedBoat = new Boat(1L, "Boat1", "Sailboat", user, null);

        when(boatRepository.save(boat)).thenReturn(savedBoat);

        Boat result = boatService.createBoat(boat);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Boat1", result.getName());
        verify(boatRepository, times(1)).save(boat);
    }

    @Test
    void shouldDeleteBoatById() {
        doNothing().when(boatRepository).deleteById(1L);

        assertDoesNotThrow(() -> boatService.deleteBoat(1L));

        verify(boatRepository, times(1)).deleteById(1L);
    }

    @Test
    void shouldThrowException_WhenDeleteByIdFails() {
        doThrow(new RuntimeException("Delete Failed")).when(boatRepository).deleteById(1L);

        Exception exception = assertThrows(RuntimeException.class, () -> boatService.deleteBoat(1L));
        assertEquals("Delete Failed", exception.getMessage());

        verify(boatRepository, times(1)).deleteById(1L);
    }
}