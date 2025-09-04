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

    public Point addPoint(@Valid Point point) {
        return pointRepository.save(point);
    }

    public List<Point> findByRoute(Route route) {
        return pointRepository.findByRouteOrderByOrderIndexAsc(route);
    }

    public List<Point> findPointsByRouteIdOrdered(Long routeId) {
        return pointRepository.findByRoute_IdOrderByOrderIndexAsc(routeId);
    }

    public Point addPointToRoute(Long routeId, @Valid Point newPoint) {
        Route route = routeRepository.findById(routeId)
                .orElseThrow(() -> new IllegalArgumentException("Route " + routeId + " not found"));
        int nextOrder = pointRepository.findTopByRoute_IdOrderByOrderIndexDesc(routeId)
                .map(p -> p.getOrderIndex() == null ? 0 : p.getOrderIndex() + 1)
                .orElse(0);
        newPoint.setRoute(route);
        if (newPoint.getOrderIndex() == null) {
            newPoint.setOrderIndex(nextOrder);
        }
        return pointRepository.save(newPoint);
    }
}