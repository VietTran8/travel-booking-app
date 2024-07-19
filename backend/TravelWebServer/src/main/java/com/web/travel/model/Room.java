package com.web.travel.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.web.travel.model.enums.ERoom;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private ERoom type;
    private double price;
    @ManyToOne
    @JoinColumn(name = "hotelId")
    @JsonIgnore
    private Hotel hotel;
}
