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

    List<Point> findByRouteOrderByOrderIndexAsc(Route route);


    List<Point> findByRoute_IdOrderByOrderIndexAsc(Long routeId);

    Optional<Point> findTopByRoute_IdOrderByOrderIndexDesc(Long routeId);

    List<Point> id(Long id);

    @Modifying
    @Query("delete from Point p where p.route.id = :routeId")
    void deleteByRouteId(@Param("routeId") Long routeId);
}