package com.biketrip.biketrip_planner.classes;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Setter
@Getter
public class Route {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
@NotBlank (message = "Route name cannot be blank")
    private String name;
@NotNull
@Column(columnDefinition = "varchar(255) default 'Poland'")
    private String country = "Poland";
@NotBlank(message = "City cannot be blank")
    private String city;
@Min(0)
    private double distance;
@Min(0)
    private double duration;
@NotNull(message = "Difficulty is required (easy/medium/hard)")
@Enumerated(EnumType.STRING)
    private Difficulty difficulty;

    @ManyToOne
    @JoinColumn( name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "route")
    private List<Point>points;

    @OneToMany(mappedBy = "route")
    private List<Review> reviews;

    @ManyToMany
    @JoinTable(
            name = "user_route",
            joinColumns = @JoinColumn(name = "route_id"),
            inverseJoinColumns = @JoinColumn (name = "user_id")
    )
    private List<User> users;

}
