package com.biketrip.biketrip_planner.service;

import com.biketrip.biketrip_planner.classes.Point;
import com.biketrip.biketrip_planner.classes.Route;
import com.biketrip.biketrip_planner.repository.RouteRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class RouteService {

    private final RouteRepository routeRepository;
    private final RestTemplate restTemplate = new RestTemplate();
    private static final double DEFAULT_MET = 7.0 ;
    @Value("${app.owm.api-key:}")
    private String apiKey;


    public RouteService(RouteRepository routeRepository) {
        this.routeRepository = routeRepository;
    }

    public double calculateCalories (Long routeId, double userWeight) {
       Route route = routeRepository.findById(routeId).orElseThrow();
        return DEFAULT_MET * userWeight * route.getDuration();
    }



    public Route createRoute(@Valid Route route) {
        List<Point> points = route.getPoints();
        if (points != null) {
            for (int i = 0; i < points.size(); i++) {
                points.get(i).setRoute(route);
            }

        }
        return routeRepository.save(route);
    }
    public Map<String, Object> getWeather(String city) {
        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + city + ",PL&appid=" + apiKey + "&units=metric";
        return restTemplate.getForObject(url, Map.class);
    }


    public Optional<Route>  findById(Long id) {
        return routeRepository.findById(id);
    }
    public List<Route> findByCity(String city) {
        return routeRepository.findByCity(city);
    }

    public void deleteById(Long id) {
        routeRepository.deleteById(id);
    }

    public List<Route> findAll() {
        return routeRepository.findAll();
    }
    public Route save(Route r){ return routeRepository.save(r); }
}
