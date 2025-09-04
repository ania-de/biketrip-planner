package com.biketrip.biketrip_planner.controller;

import com.biketrip.biketrip_planner.classes.Category;
import com.biketrip.biketrip_planner.classes.Route;
import com.biketrip.biketrip_planner.dto.DtoMapper;
import com.biketrip.biketrip_planner.dto.RouteCreateRequest;
import com.biketrip.biketrip_planner.dto.RouteDetailsResponse;
import com.biketrip.biketrip_planner.service.CategoryService;
import com.biketrip.biketrip_planner.service.ReviewService;
import com.biketrip.biketrip_planner.service.RouteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/routes")
public class RouteController {

    private final RouteService routeService;
    private final CategoryService categoryService;
    private final ReviewService reviewService;

    public RouteController(RouteService routeService, CategoryService categoryService, ReviewService reviewService) {
        this.routeService = routeService;
        this.categoryService = categoryService;
        this.reviewService = reviewService;
    }

    @PostMapping
    public ResponseEntity<RouteDetailsResponse> create(@RequestBody @Valid RouteCreateRequest req) {
        Category category = null;
        if (req.categoryId() != null) {
            category = categoryService.findById(req.categoryId())
                    .orElseThrow(() -> new IllegalArgumentException("Category not found: " + req.categoryId()));
        }
        Route saved = routeService.createRoute(DtoMapper.fromCreate(req, category));
        double avg = reviewService.calculateAverageRating(saved.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(DtoMapper.toDetails(saved, avg));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RouteDetailsResponse> get(@PathVariable Long id) {
        return routeService.findById(id)
                .map(r -> DtoMapper.toDetails(r, reviewService.calculateAverageRating(id)))
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping
    public List<RouteDetailsResponse> list(@RequestParam(required = false) String city) {
        List<Route> routes = (city == null || city.isBlank()) ? routeService.findAll() : routeService.findByCity(city);
        return routes.stream()
                .map(r -> DtoMapper.toDetails(r, reviewService.calculateAverageRating(r.getId())))
                .toList();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        routeService.deleteById(id);
    }
    @PutMapping("/{id}")
    public ResponseEntity<Route> update(@PathVariable Long id, @RequestBody @Valid Route in){
        return routeService.findById(id).map(r -> {
            r.setName(in.getName());
            r.setCity(in.getCity());
            r.setCountry(in.getCountry());
            r.setDifficulty(in.getDifficulty());
            r.setDistance(in.getDistance());   // km
            r.setDuration(in.getDuration());   // godz
            return ResponseEntity.ok(routeService.save(r));
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }
}

