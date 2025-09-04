package com.biketrip.biketrip_planner.dto;

import com.biketrip.biketrip_planner.classes.Difficulty;

import java.util.List;

public record RouteDetailsResponse(
        Long id,
        String name,
        String city,
        String country,
        Difficulty difficulty,
        double distance,
        double duration,
        String category,
        double averageRating,
        List<PointDTO> points
) {}
