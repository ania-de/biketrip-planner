package com.biketrip.biketrip_planner.repository;

import com.biketrip.biketrip_planner.classes.Point;
import com.biketrip.biketrip_planner.classes.Route;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PointRepository extends JpaRepository<Point, Long> {
    List<Point> findByRoute(Route route);

    List<Point> findPointsByRouteIdOrderByIdAsc(Long routeId);
}
