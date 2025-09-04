package com.biketrip.biketrip_planner.repository;

import com.biketrip.biketrip_planner.classes.Route;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RouteRepository extends JpaRepository<Route, Long> {
    List<Route> findByCity(String city);

}
