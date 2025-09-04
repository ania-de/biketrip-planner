package com.biketrip.biketrip_planner.weather;

import java.util.List;

public record OwmCurrentResponse(
        Main main, Wind wind, List<Weather> weather, String name
) {
    public record Main(double temp, double feels_like, int humidity) {}
    public record Wind(double speed) {}
    public record Weather(String description, String icon) {}
}
