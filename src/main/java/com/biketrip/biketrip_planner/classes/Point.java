package com.biketrip.biketrip_planner.classes;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Point {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @DecimalMin(value = "-90.0")  @DecimalMax(value = "90.0")
    private double latitude;
    @DecimalMin(value = "-180.0") @DecimalMax(value = "180.0")
    private double longitude;

    @Column(name = "point_order")
    private Integer orderIndex;

    @ManyToOne
    @JoinColumn(name = "route_id")
    private Route route;
}
