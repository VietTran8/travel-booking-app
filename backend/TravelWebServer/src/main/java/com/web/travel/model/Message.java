package com.web.travel.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.web.travel.utils.DateHandler;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String message;
    private Long uid;
    @JsonFormat(pattern = "HH:mm:ss dd/MM/yyyy")
    private Date time = DateHandler.getCurrentDateTime();
    private Long room;
    private String name;
    private String role;
    private String avatar;
}
