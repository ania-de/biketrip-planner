package com.biketrip.biketrip_planner.repository;

import com.biketrip.biketrip_planner.classes.Difficulty;
import com.biketrip.biketrip_planner.classes.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RouteRepository extends JpaRepository<Route, Long> {
    List<Route> findByCity(String city);
    @Query("""
    select r from Route r
    where (:city is null or lower(r.city) = lower(:city))
      and (:diff is null or r.difficulty = :diff)
      and (:minDist is null or r.distance >= :minDist)
      and (:maxDist is null or r.distance <= :maxDist)
      and (:maxDur is null or r.duration <= :maxDur)
  """)
    List<Route> search(
            @Param("city") String city,
            @Param("diff") Difficulty diff,
            @Param("minDist") Double minDistanceKm,
            @Param("maxDist") Double maxDistanceKm,
            @Param("maxDur") Double maxDurationMin
    );
}

