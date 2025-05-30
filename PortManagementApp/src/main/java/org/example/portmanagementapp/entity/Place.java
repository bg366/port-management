package org.example.portmanagementapp.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "places")
public class Place {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int number;
    private double lengthLimit;
    private boolean available;

    @OneToMany(mappedBy = "place")
    @JsonManagedReference
    private List<Reservation> reservations;
}
