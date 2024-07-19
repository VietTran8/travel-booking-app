package com.web.travel.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.web.travel.model.enums.ETourDateType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "tour_date")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TourDate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private Date departDate;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private Date endDate;
    @Enumerated(EnumType.STRING)
    @JsonProperty("type")
    private ETourDateType dateType;
    private double adultPrice;
    private double childPrice;
    private int currentPeople;
    private int maxPeople;
    @ManyToOne
    @JoinColumn(name = "tourId")
    @JsonIgnore
    private Tour tour;
    @OneToMany(mappedBy = "tourDate")
    @JsonIgnore
    private List<Order> orders;

    public boolean isFull(){
        return currentPeople >= maxPeople;
    }
    public boolean canBook(int adults){
        return adults <= (maxPeople - currentPeople);
    }
}
