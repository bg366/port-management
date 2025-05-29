package org.example.portmanagementapp.repository;

import org.example.portmanagementapp.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceRepository extends JpaRepository<Place, Long> {
}
