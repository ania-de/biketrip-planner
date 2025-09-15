package com.biketrip.biketrip_planner.repository;

import com.biketrip.biketrip_planner.classes.Point;
import com.biketrip.biketrip_planner.classes.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PointRepository extends JpaRepository<Point, Long> {


    List<Point> findByRouteOrderByIdAsc(Route route);


    List<Point> findByRoute_IdOrderByIdAsc(Long routeId);


    long deleteByRoute_Id(Long routeId);
}