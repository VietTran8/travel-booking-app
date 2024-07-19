package com.web.travel.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Collection;

@Entity
@Table(name = "paragraph")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Paragraph {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 100000)
    private String content;
    @Column(name = "paragraphOrder")
    private Integer order;
    private String imgName;
    private String imgSrc;
    private int layout;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "blogId")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    private Blog blog;
}
