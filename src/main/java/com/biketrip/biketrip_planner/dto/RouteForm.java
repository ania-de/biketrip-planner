package com.biketrip.biketrip_planner.dto;

import com.biketrip.biketrip_planner.classes.Difficulty;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RouteForm {
    @NotBlank
    private String name;
    @NotBlank private String city;
    @NotNull
    private Difficulty difficulty = Difficulty.EASY;
    @DecimalMin("0.1") private double distance;
    @DecimalMin("0.1") private double durationMin;
    private Long categoryId;
}
