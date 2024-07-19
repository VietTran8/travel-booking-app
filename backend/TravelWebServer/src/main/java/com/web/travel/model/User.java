package com.web.travel.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.web.travel.model.enums.EUserStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "user")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 100)
    private String fullName;
    private String address;
    private String avatar;
    @Email
    @Column(unique = true)
    private String email;
    @Column
    @JsonIgnore
    private String password;
    @Column(length = 13)
    private String phone;
    @Enumerated(EnumType.STRING)
    private EUserStatus active;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "userId"),
            inverseJoinColumns = @JoinColumn(name = "roleId")
    )
    private Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private Collection<Order> orders;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private Collection<Rate> rates;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private Collection<DestinationBlog> destinationBlogs;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonIgnore
    private List<LoginHistory> loginHistories;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JsonIgnore
    private List<RecentActivity> recentActivities;
    public User(String fullName, String address, String email, String password, String phone) {
        this.fullName = fullName;
        this.address = address;
        this.email = email;
        this.password = password;
        this.phone = phone;
    }
}
