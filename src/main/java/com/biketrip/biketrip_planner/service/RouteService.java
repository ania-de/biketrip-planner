package com.biketrip.biketrip_planner.service;

import com.biketrip.biketrip_planner.classes.Point;
import com.biketrip.biketrip_planner.classes.Route;
import com.biketrip.biketrip_planner.repository.RouteRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class RouteService {

    private final RouteRepository routeRepository;
    private final RestTemplate restTemplate = new RestTemplate();
    private static final double MET = 7.0 ;


    public RouteService(RouteRepository routeRepository) {
        this.routeRepository = routeRepository;
    }

    public double calculateCalories (Long routeId, double userWeight) {
       Route route = routeRepository.findById(routeId).orElseThrow();
        return MET * userWeight * route.getDuration();
    }

//    public Route createRoute(@Valid Route route) {
//        List<Point> points = route.getPoints();
//        if (points != null)
//
//
//    }
}
