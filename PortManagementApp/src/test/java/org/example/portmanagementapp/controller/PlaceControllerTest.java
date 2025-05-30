package org.example.portmanagementapp.controller;

import org.example.portmanagementapp.entity.Place;
import org.example.portmanagementapp.service.PlaceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PlaceControllerTest {

    @Mock
    private PlaceService placeService;

    @InjectMocks
    private PlaceController placeController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreatePlace() {
        Place place = new Place();
        place.setId(1L);
        place.setNumber(101);
        place.setLengthLimit(50.5);
        place.setAvailable(true);

        when(placeService.createPlace(place)).thenReturn(place);

        ResponseEntity<Place> response = placeController.createPlace(place);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals(place, response.getBody());
        verify(placeService, times(1)).createPlace(place);
    }

    @Test
    void testGetAllPlaces() {
        Place place1 = new Place();
        place1.setId(1L);
        place1.setNumber(101);
        place1.setLengthLimit(50.5);
        place1.setAvailable(true);

        Place place2 = new Place();
        place2.setId(2L);
        place2.setNumber(102);
        place2.setLengthLimit(70.0);
        place2.setAvailable(false);

        when(placeService.getAllPlaces()).thenReturn(Arrays.asList(place1, place2));

        List<Place> places = placeController.getAllPlaces();

        assertEquals(2, places.size());
        assertEquals(place1, places.get(0));
        assertEquals(place2, places.get(1));
        verify(placeService, times(1)).getAllPlaces();
    }

    @Test
    void testGetPlaceById_Found() {
        Place place = new Place();
        place.setId(1L);
        place.setNumber(101);
        place.setLengthLimit(50.5);
        place.setAvailable(true);

        when(placeService.getPlaceById(1L)).thenReturn(Optional.of(place));

        ResponseEntity<Place> response = placeController.getPlaceById(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(place, response.getBody());
        verify(placeService, times(1)).getPlaceById(1L);
    }

    @Test
    void testGetPlaceById_NotFound() {
        when(placeService.getPlaceById(1L)).thenReturn(Optional.empty());

        ResponseEntity<Place> response = placeController.getPlaceById(1L);

        assertEquals(404, response.getStatusCodeValue());
        verify(placeService, times(1)).getPlaceById(1L);
    }
}