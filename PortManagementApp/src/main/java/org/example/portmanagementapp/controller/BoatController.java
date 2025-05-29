package org.example.portmanagementapp.controller;

import lombok.RequiredArgsConstructor;
import org.example.portmanagementapp.entity.Boat;
import org.example.portmanagementapp.service.BoatService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/boats")
@RequiredArgsConstructor
public class BoatController {

    private final BoatService boatService;

    @PostMapping
    public ResponseEntity<Boat> createBoat(@RequestBody Boat boat) {
        Boat saved = boatService.createBoat(boat);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping
    public List<Boat> getAllBoats() {
        return boatService.getAllBoats();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Boat> getBoatById(@PathVariable Long id) {
        return boatService.getBoatById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
