package com.biketrip.biketrip_planner.controller;

import com.biketrip.biketrip_planner.classes.Route;
import com.biketrip.biketrip_planner.service.RouteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/routes")
public class RouteController {
    private final RouteService routeService;
    public RouteController(RouteService routeService) {
        this.routeService = routeService;
    }
    @PostMapping
    public ResponseEntity<Route> createRoute(@RequestBody Route route) {
        return ResponseEntity.ok(routeService.createRoute(route));
    }
}
