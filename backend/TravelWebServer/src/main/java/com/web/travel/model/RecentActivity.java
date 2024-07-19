package com.web.travel.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.web.travel.model.enums.ERecentActivityType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RecentActivity {
    @ManyToOne()
    @JoinColumn(name = "blogId")
    private DestinationBlog blog;
    @ManyToOne()
    @JoinColumn(name = "tourId")
    private Tour tour;
    @ManyToOne()
    @JoinColumn(name = "userId")
    private User user;
    @JsonFormat(pattern = "HH:mm:ss dd/MM/yyyy")
    private Date time;
    @Enumerated(EnumType.STRING)
    private ERecentActivityType type;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
}
