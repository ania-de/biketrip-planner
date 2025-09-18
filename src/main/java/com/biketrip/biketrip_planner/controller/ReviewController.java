package com.biketrip.biketrip_planner.controller;

import com.biketrip.biketrip_planner.classes.Review;
import com.biketrip.biketrip_planner.classes.Route;
import com.biketrip.biketrip_planner.dto.DtoMapper;
import com.biketrip.biketrip_planner.dto.ReviewRequest;
import com.biketrip.biketrip_planner.dto.ReviewResponse;
import com.biketrip.biketrip_planner.service.ReviewService;
import com.biketrip.biketrip_planner.service.RouteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;
    private final RouteService routeService;

    public ReviewController(ReviewService reviewService, RouteService routeService) {
        this.reviewService = reviewService;
        this.routeService = routeService;
    }

    @PostMapping("/route/{routeId}")
    public ResponseEntity<ReviewResponse> create(@PathVariable Long routeId, @RequestBody @Valid ReviewRequest req) {
        Route route = routeService.findById(routeId).orElseThrow();
        Review r = new Review();
        r.setRoute(route);
        r.setContent(req.content());
        r.setRating(req.rating());
        Review saved = reviewService.createReview(r);
        return ResponseEntity.status(HttpStatus.CREATED).body(DtoMapper.toReview(saved));
    }

    @GetMapping("/route/{routeId}")
    public List<ReviewResponse> listForRoute(@PathVariable Long routeId) {
        return reviewService.findReviewsByRouteId(routeId).stream().map(DtoMapper::toReview).toList();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReviewResponse> update(@PathVariable Long id, @RequestBody @Valid ReviewRequest req) {
        Review existing = reviewService.findById(id).orElseThrow();
        existing.setContent(req.content());
        existing.setRating(req.rating());
        Review saved = reviewService.createReview(existing);
        return ResponseEntity.ok(DtoMapper.toReview(saved));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        reviewService.deleteById(id);
    }

    @GetMapping("/route/{routeId}/avg")
    public Optional<Map<String, Object>> average(@PathVariable Long routeId) {
        return routeService.findById(routeId)
                .map(route -> Map.of(
                        "route: ", route,
                        "Średnia ocen: ", reviewService.calculateAverageRating(routeId)
                ));

    }

}



