package com.biketrip.biketrip_planner.dto;
import com.biketrip.biketrip_planner.classes.Category;
import com.biketrip.biketrip_planner.classes.Point;
import com.biketrip.biketrip_planner.classes.Review;
import com.biketrip.biketrip_planner.classes.Route;

import java.util.ArrayList;
import java.util.List;

public class DtoMapper {
    public static Route fromCreate(RouteCreateRequest r, Category category) {
        Route route = new Route();
        route.setName(r.name());
        route.setCity(r.city());
        route.setCountry("Poland");
        route.setDifficulty(r.difficulty());
        route.setDistance(r.distance());
        route.setDuration(r.duration());
        route.setCategory(category);
        if (r.points() != null && !r.points().isEmpty()) {
            List<Point> list = new ArrayList<>();
            for (int i = 0; i < r.points().size(); i++) {
                PointDTO dto = r.points().get(i);
                Point p = new Point();
                p.setName(dto.name());
                p.setLatitude(dto.latitude());
                p.setLongitude(dto.longitude());
                p.setOrderIndex(dto.orderIndex() != null ? dto.orderIndex() : i);
                p.setRoute(route);
                list.add(p);
            }
            route.setPoints(list);
        }
        return route;
    }

    public static RouteDetailsResponse toDetails(Route route, double avg) {
        List<PointDTO> pts = route.getPoints() == null ? List.of() :
                route.getPoints().stream()
                        .sorted((a,b) -> Integer.compare(
                                a.getOrderIndex() == null ? 0 : a.getOrderIndex(),
                                b.getOrderIndex() == null ? 0 : b.getOrderIndex()))
                        .map(p -> new PointDTO(p.getName(), p.getLatitude(), p.getLongitude(), p.getOrderIndex()))
                        .toList();

        return new RouteDetailsResponse(
                route.getId(),
                route.getName(),
                route.getCity(),
                route.getCountry(),
                route.getDifficulty(),
                route.getDistance(),
                route.getDuration(),
                route.getCategory() != null ? route.getCategory().getName() : null,
                avg,
                pts
        );
    }

    public static ReviewResponse toReview(Review r) {
        return new ReviewResponse(r.getId(), r.getRoute() != null ? r.getRoute().getId() : null, r.getRating(), r.getContent());
    }
}

