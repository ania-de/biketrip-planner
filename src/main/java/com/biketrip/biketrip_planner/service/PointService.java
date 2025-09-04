package com.biketrip.biketrip_planner.service;

import com.biketrip.biketrip_planner.classes.Point;
import com.biketrip.biketrip_planner.classes.Route;
import com.biketrip.biketrip_planner.repository.PointRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class PointService {
    private final PointRepository pointRepository;

    public PointService(PointRepository pointRepository) {
        this.pointRepository = pointRepository;
    }
    public Point addPoint(Point point) {
        return pointRepository.save(point);
    }
    public List<Point> findByRoute(Route route) {
        return pointRepository.findByRoute(route);
    }
    public List<Point> findPointsByRouteIdOrdered(Long routeId) {
        return pointRepository.findPointsByRouteIdOrderByIdAsc(routeId);

    }
    public void addPointToRoute(Long routeId, Point newPoint) {
    }
}
