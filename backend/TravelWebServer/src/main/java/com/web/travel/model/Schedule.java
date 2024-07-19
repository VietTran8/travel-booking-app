package com.web.travel.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Table(name = "schedule")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String time;
    @Size(max = 10000)
    private String content;
    @ManyToOne
    @JoinColumn(name = "tourId")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    private Tour tour;

    public Schedule(String time, String content, Tour tour) {
        this.time = time;
        this.content = content;
        this.tour = tour;
    }
}
