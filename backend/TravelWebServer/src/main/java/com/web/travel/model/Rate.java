package com.web.travel.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Rate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int point;
    private String comment;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private Date dateRated;
    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;
    @ManyToOne
    @JoinColumn(name = "tourId")
    private Tour tour;
    @ManyToOne
    @JoinColumn(name = "blogId")
    private DestinationBlog blog;

    public Rate(int point, String comment, Tour tour) {
        this.point = point;
        this.comment = comment;
        this.tour = tour;
    }
}
