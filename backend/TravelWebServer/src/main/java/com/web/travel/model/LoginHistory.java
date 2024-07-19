package com.web.travel.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class LoginHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonFormat(pattern = "HH:mm:ss dd/MM/yyyy")
    private Date loggedDate;
    private String userDevice;
    private String ipAddress;
    private String avatar;
    @ManyToOne
    @JoinColumn(name = "userId")
    @JsonIgnore
    private User user;

    public LoginHistory(LoginHistory loginHistory){
        this.id = loginHistory.getId();
        this.loggedDate = loginHistory.getLoggedDate();
        this.userDevice = loginHistory.getUserDevice();
        this.ipAddress = loginHistory.getIpAddress();
        this.avatar = loginHistory.getAvatar();
        this.user = loginHistory.getUser();
    }
}
