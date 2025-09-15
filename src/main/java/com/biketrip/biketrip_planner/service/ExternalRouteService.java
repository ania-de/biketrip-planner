package com.biketrip.biketrip_planner.service;

import com.biketrip.biketrip_planner.classes.Difficulty;
import com.biketrip.biketrip_planner.classes.Point;
import com.biketrip.biketrip_planner.classes.Route;
import com.biketrip.biketrip_planner.repository.PointRepository;
import com.biketrip.biketrip_planner.repository.RouteRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Service
public class ExternalRouteService {

    private final RestClient ors;
    private final RestClient nominatim;
    private final RouteRepository routeRepo;
    private final PointRepository pointRepo;

    public ExternalRouteService(
            @Value("${app.ors.base-url}") String orsBase,
            @Value("${app.ors.api-key}") String orsKey,
            RouteRepository routeRepo, PointRepository pointRepo) {

        this.ors = RestClient.builder()
                .baseUrl(orsBase)
                .defaultHeader("Authorization", orsKey)
                .build();

        this.nominatim = RestClient.builder()
                .baseUrl("https://nominatim.openstreetmap.org")
                .defaultHeader("User-Agent", "BikeTripPlanner/1.0 (contact@example.com)")
                .build();

        this.routeRepo = routeRepo;
        this.pointRepo = pointRepo;
    }

    public Route generateRoundTrip(String city, double targetKm) {

        var geo = nominatim.get().uri(uri -> uri.path("/search")
                        .queryParam("q", city + ", Poland")
                        .queryParam("format", "json")
                        .queryParam("limit", 1)
                        .build())
                .retrieve()
                .body(new ParameterizedTypeReference<List<Map<String,Object>>>() {});
        if (geo == null || geo.isEmpty()) {
            throw new IllegalArgumentException("Nie znaleziono miasta: " + city);
        }
        double lat = Double.parseDouble((String) geo.get(0).get("lat"));
        double lon = Double.parseDouble((String) geo.get(0).get("lon"));


        Map<String,Object> body = Map.of(
                "coordinates", List.of(List.of(lon, lat)),
                "options", Map.of("round_trip", Map.of(
                        "length", Math.round(targetKm * 1000),
                        "points", 12,
                        "seed", 3
                ))
        );

        Map<String,Object> resp = ors.post()
                .uri("/v2/directions/cycling-regular/geojson")
                .body(body)
                .retrieve()
                .body(new ParameterizedTypeReference<Map<String,Object>>() {});

        var features = (List<Map<String,Object>>) resp.get("features");
        if (features == null || features.isEmpty()) {
            throw new IllegalStateException("Brak trasy z ORS");
        }
        var f0 = features.get(0);
        var props = (Map<String,Object>) f0.get("properties");
        var summary = (Map<String,Object>) props.get("summary");
        double distKm = ((Number) summary.get("distance")).doubleValue() / 1000.0;
        double durMin = ((Number) summary.get("duration")).doubleValue() / 60.0;


        Route r = new Route();
        r.setName("Pętla " + city + " ~" + Math.round(targetKm) + " km");
        r.setCity(city);
        r.setCountry("Poland");
        r.setDifficulty(Difficulty.EASY);
        r.setDistance(distKm);
        r.setDuration(durMin);
        r = routeRepo.save(r);


        var geom = (Map<String,Object>) f0.get("geometry");
        var coords = (List<List<Double>>) geom.get("coordinates");
        int i = 0;
        for (var c : coords) {
            Point p = new Point();
            p.setRoute(r);
            p.setName("P" + (++i));
            p.setLongitude(c.get(0));
            p.setLatitude(c.get(1));
            pointRepo.save(p);
        }
        return r;
    }
}
