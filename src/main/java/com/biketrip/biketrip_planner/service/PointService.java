package com.biketrip.biketrip_planner.service;

import com.biketrip.biketrip_planner.classes.Point;
import com.biketrip.biketrip_planner.classes.Route;
import com.biketrip.biketrip_planner.repository.PointRepository;
import com.biketrip.biketrip_planner.repository.RouteRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@Transactional
public class PointService {

    private final PointRepository pointRepository;
    private final RouteRepository routeRepository;

    public PointService(PointRepository pointRepository, RouteRepository routeRepository) {
        this.pointRepository = pointRepository;
        this.routeRepository = routeRepository;
    }

    public Point addPoint(Point point) {
        return pointRepository.save(point);
    }

    public List<Point> findByRoute(Route route) {
        return pointRepository.findByRouteOrderByIdAsc(route);
    }

    public List<Point> findPointsByRouteIdOrdered(Long routeId) {
        return pointRepository.findByRoute_IdOrderByIdAsc(routeId);
    }

    public Point addPointToRoute(Long routeId, Point newPoint) {
        Route route = routeRepository.findById(routeId)
                .orElseThrow(() -> new IllegalArgumentException("Route " + routeId + " not found"));
        newPoint.setRoute(route);
        return pointRepository.save(newPoint);
    }
    public List<Point> findFirst10ByRouteId(Long routeId) {
        List<Point> all = pointRepository.findByRoute_IdOrderByIdAsc(routeId);
        return all.size() <= 10 ? all : all.subList(0, 10);
    }
}