package org.example.portmanagementapp.repository;

import org.example.portmanagementapp.entity.Boat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoatRepository extends JpaRepository<Boat, Long> {
}
