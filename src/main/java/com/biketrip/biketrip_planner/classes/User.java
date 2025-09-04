package com.biketrip.biketrip_planner.classes;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Username cannot be blank")
    private String username;
    @Email(message = " Email is not valid")
    private String email;
    @NotBlank(message = "Password cannot be empty")
    private String password;
    @Min(value = 30, message = "Weight should be at least 30kg")
    private double weight;

    @ManyToMany
    private List<Route> routes;
}
