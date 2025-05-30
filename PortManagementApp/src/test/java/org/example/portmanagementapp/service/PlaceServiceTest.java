package org.example.portmanagementapp.service;

import org.example.portmanagementapp.entity.Place;
import org.example.portmanagementapp.repository.PlaceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PlaceServiceTest {

    private PlaceRepository placeRepository;
    private PlaceService placeService;

    @BeforeEach
    void setUp() {
        placeRepository = mock(PlaceRepository.class);
        placeService = new PlaceService(placeRepository);
    }

    @Test
    void shouldReturnAllPlaces() {
        Place p1 = new Place();
        Place p2 = new Place();

        when(placeRepository.findAll()).thenReturn(Arrays.asList(p1, p2));

        assertEquals(2, placeService.getAllPlaces().size());
    }

    @Test
    void shouldReturnPlaceById() {
        Place place = new Place();
        place.setId(1L);

        when(placeRepository.findById(1L)).thenReturn(Optional.of(place));

        Optional<Place> found = placeService.getPlaceById(1L);
        assertEquals(1L, found.get().getId());
    }

    @Test
    void shouldAddNewPlace() {
        Place place = new Place();
        place.setId(1L);

        when(placeRepository.save(place)).thenReturn(place);

        Place saved = placeService.createPlace(place);
        assertEquals(1L, saved.getId());
    }

    @Test
    void shouldDeletePlace() {
        doNothing().when(placeRepository).deleteById(1L);
        assertDoesNotThrow(() -> placeService.deletePlace(1L));
        verify(placeRepository, times(1)).deleteById(1L);
    }
}
