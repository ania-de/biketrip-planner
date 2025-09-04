package com.biketrip.biketrip_planner.weather;

public record WeatherDTO(
        String city, double tempC, double windMs, String description, String icon
) {}
