package com.biketrip.biketrip_planner.exam;

import jakarta.persistence.*;

@Entity
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private double salary;
   @OneToOne
    @JoinColumn(name = "department_id")
    private Department department;

}
