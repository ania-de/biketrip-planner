package com.biketrip.biketrip_planner.dto;

public record ReviewResponse(
        Long id,
        Long routeId,
        int rating,
        String content
) {}