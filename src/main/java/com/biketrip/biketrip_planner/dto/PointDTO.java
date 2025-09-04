package com.biketrip.biketrip_planner.dto;

public record PointDTO(
        String name,
        double latitude,
        double longitude,
        Integer orderIndex
) {}
