package com.dawn.cinema.model;

import com.dawn.common.model.AbstractMappedEntity;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Hidden
@Entity
@Table(name = "theater")
@SuperBuilder
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Theater extends AbstractMappedEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "location", nullable = false)
    private String location;

    @Column(name = "capacity", nullable = false)
    private Integer capacity;

    @Column(name = "is_deleted", nullable = false)
    @Builder.Default
    private Boolean isDeleted = false;

    @OneToMany(mappedBy = "theater", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Showtime> showtime = new ArrayList<>();
}
