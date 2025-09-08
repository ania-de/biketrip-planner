package com.biketrip.biketrip_planner.dto;

import com.biketrip.biketrip_planner.weather.WeatherDTO;

public record RouteWeatherResponse(RouteDetailsResponse route, WeatherDTO weather) {}
