package com.web.travel.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.web.travel.model.enums.ETourType;
import jakarta.persistence.*;
import lombok.*;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "tour")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tour {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String vehicle;
    @Enumerated(EnumType.STRING)
    private ETourType tourType;
    private String depart;
    private String destination;
    private Boolean isRemoved;
    @OneToMany(mappedBy = "tour",
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL
    )
    @JsonIgnore
    private Collection<TourDate> tourDate;
    private String img;
    @OneToMany(mappedBy = "tour",
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER,
            orphanRemoval = true
    )
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    private Collection<Schedule> schedules;
    @OneToMany(mappedBy = "tour", fetch = FetchType.EAGER)
    @JsonIgnore
    private Collection<Order> orders;
    @OneToMany(mappedBy = "tour", fetch = FetchType.EAGER)
    @JsonIgnore
    private Collection<Rate> rates;
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "tour_hotel",
            joinColumns = @JoinColumn(name = "tourId"),
            inverseJoinColumns = @JoinColumn(name = "hotelId")
    )
    @JsonIgnore
    private Collection<Hotel> hotels;
    @OneToMany(mappedBy = "tour")
    @JsonIgnore
    private List<RecentActivity> recentActivities;
}
