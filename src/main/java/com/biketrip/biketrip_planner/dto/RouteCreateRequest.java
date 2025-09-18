package com.biketrip.biketrip_planner.dto;

import com.biketrip.biketrip_planner.classes.Difficulty;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record RouteCreateRequest(
        @NotBlank String name,
        @NotBlank String city,
        @NotNull Difficulty difficulty,
        @DecimalMin("0.1") double distance,
        @DecimalMin("0.1") double duration,
        Long categoryId,
        List<PointDTO> points
) {}
