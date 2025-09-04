package com.biketrip.biketrip_planner.dto;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record ReviewRequest(
        @NotBlank String content,
        @Min(1) @Max(5) int rating
) {}
