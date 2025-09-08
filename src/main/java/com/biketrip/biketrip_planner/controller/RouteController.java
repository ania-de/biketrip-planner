package com.biketrip.biketrip_planner.controller;

import com.biketrip.biketrip_planner.classes.Category;
import com.biketrip.biketrip_planner.classes.Route;
import com.biketrip.biketrip_planner.dto.DtoMapper;
import com.biketrip.biketrip_planner.dto.RouteCreateRequest;
import com.biketrip.biketrip_planner.dto.RouteDetailsResponse;
import com.biketrip.biketrip_planner.dto.RouteWeatherResponse;
import com.biketrip.biketrip_planner.service.CategoryService;
import com.biketrip.biketrip_planner.service.ReviewService;
import com.biketrip.biketrip_planner.service.RouteService;
import com.biketrip.biketrip_planner.weather.WeatherDTO;
import com.biketrip.biketrip_planner.weather.WeatherService;
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
    private final WeatherService weatherService;

    public RouteController(RouteService routeService, CategoryService categoryService, ReviewService reviewService,WeatherService weatherService) {
        this.routeService = routeService;
        this.categoryService = categoryService;
        this.reviewService = reviewService;
        this.weatherService = weatherService;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody @Valid RouteCreateRequest req,
                                    @RequestParam(defaultValue = "false") boolean includeWeather) {
        Category category = null;
        if (req.categoryId() != null) {
            category = categoryService.findById(req.categoryId())
                    .orElseThrow(() -> new IllegalArgumentException("Category not found: " + req.categoryId()));
        }
        Route saved = routeService.createRoute(DtoMapper.fromCreate(req, category));
        double avg = reviewService.calculateAverageRating(saved.getId());
        RouteDetailsResponse details = DtoMapper.toDetails(saved, avg);

        if (includeWeather) {
            WeatherDTO w = weatherService.getCityWeather(saved.getCity());
            return ResponseEntity.status(HttpStatus.CREATED).body(new RouteWeatherResponse(details, w));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(details);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable Long id,
                                 @RequestParam(defaultValue = "false") boolean includeWeather) {
        return routeService.findById(id)
                .map(r -> {
                    RouteDetailsResponse details = DtoMapper.toDetails(r, reviewService.calculateAverageRating(id));
                    if (includeWeather) {
                        WeatherDTO w = weatherService.getCityWeather(r.getCity());
                        return ResponseEntity.ok(new RouteWeatherResponse(details, w));
                    }
                    return ResponseEntity.ok(details);
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping
    public ResponseEntity<?> list(@RequestParam(required = false) String city,
                                  @RequestParam(defaultValue = "false") boolean includeWeather) {
        List<Route> routes = (city == null || city.isBlank())
                ? routeService.findAll()
                : routeService.findByCity(city);

        if (!includeWeather) {
            var list = routes.stream()
                    .map(r -> DtoMapper.toDetails(r, reviewService.calculateAverageRating(r.getId())))
                    .toList();
            return ResponseEntity.ok(list);
        } else {
            var list = routes.stream()
                    .map(r -> {
                        var details = DtoMapper.toDetails(r, reviewService.calculateAverageRating(r.getId()));
                        var w = weatherService.getCityWeather(r.getCity());
                        return new RouteWeatherResponse(details, w);
                    })
                    .toList();
            return ResponseEntity.ok(list);
        }
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
            r.setDistance(in.getDistance());
            r.setDuration(in.getDuration());
            return ResponseEntity.ok(routeService.save(r));
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }
}

