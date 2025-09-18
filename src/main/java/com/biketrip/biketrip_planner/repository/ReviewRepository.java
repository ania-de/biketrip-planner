package com.biketrip.biketrip_planner.repository;

import com.biketrip.biketrip_planner.classes.Review;
import com.biketrip.biketrip_planner.classes.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByRouteId(Long routeId);

    List<Review> findByRoute(Route route);

    @Modifying
    @Query("delete from Review r where r.route.id = :routeId")
    void deleteByRouteId(@Param("routeId") Long routeId);

    @Query("select avg(r.rating) from Review r where r.route.id = :routeId")
    Double avgRatingForRoute(@Param("routeId") Long routeId);




}
