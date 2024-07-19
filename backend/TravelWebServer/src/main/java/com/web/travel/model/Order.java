package com.web.travel.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.web.travel.model.enums.EOrderStatus;
import com.web.travel.model.enums.EPaymentMethod;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "orders")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private Date orderDate = new Date();
    private int adults;
    private int children;
    @Enumerated(EnumType.STRING)
    private EPaymentMethod paymentMethod;
    @Enumerated(EnumType.STRING)
    private EOrderStatus status;
    private double totalPrice;
    private String roomType;
    private String specialRequest;
    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;
    @ManyToOne
    @JoinColumn(name = "tourId")
    private Tour tour;
    @ManyToOne
    @JoinColumn(name = "tourDateId")
    private TourDate tourDate;
    @OneToOne
    @JoinColumn(name = "contactInfoId")
    private ContactInfo contactInfo;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "hotelId")
    private Hotel hotel;

    public int getTotalPeople(){
        return getAdults() + (int)(getChildren()/2);
    }
}
