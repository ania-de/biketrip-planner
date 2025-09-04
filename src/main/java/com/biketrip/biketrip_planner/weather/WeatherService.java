package com.biketrip.biketrip_planner.weather;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriUtils;

import java.nio.charset.StandardCharsets;
import java.util.Locale;

@Service
public class WeatherService {

    private final RestClient owm;
    private final String apiKey;

    public WeatherService(
            @Value("${app.owm.base-url:https://api.openweathermap.org/data/2.5}") String baseUrl,
            @Value("${app.owm.api-key:}") String apiKey) {

        System.out.println("DEBUG baseUrl=" + baseUrl);
        System.out.println("DEBUG apiKey len=" + (apiKey == null ? "null" : apiKey.length()));


        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException("Brak klucza OpenWeatherMap. Ustaw OWM_API_KEY w Environment variables albo app.owm.api-key.");
        }
        this.owm = org.springframework.web.client.RestClient.builder().baseUrl(baseUrl).build();
        this.apiKey = apiKey;
    }


    public WeatherDTO getCityWeather(String city) {
        String q = UriUtils.encodeQueryParam(city, StandardCharsets.UTF_8);

        OwmCurrentResponse owmResp = owm.get()
                .uri(uri -> uri.path("/weather")
                        .queryParam("q", q)
                        .queryParam("appid", apiKey)
                        .queryParam("units", "metric")
                        .build())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (req, res) -> {
                    throw new ResponseStatusException(org.springframework.http.HttpStatus.BAD_REQUEST,
                            "City not found or bad request");
                })
                .onStatus(HttpStatusCode::is5xxServerError, (req, res) -> {
                    throw new ResponseStatusException(org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE,
                            "Weather provider unavailable");
                })
                .body(OwmCurrentResponse.class);

        if (owmResp == null || owmResp.main() == null || owmResp.weather() == null || owmResp.weather().isEmpty()) {
            throw new ResponseStatusException(org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE,
                    "Invalid weather response");
        }

        var w = owmResp.weather().get(0);
        return new WeatherDTO(
                owmResp.name() != null ? owmResp.name() : capitalize(city),
                owmResp.main().temp(),
                owmResp.wind() != null ? owmResp.wind().speed() : 0.0,
                w.description(),
                w.icon()
        );
    }

    private static String capitalize(String s) {
        if (s == null || s.isBlank()) return s;
        return s.substring(0,1).toUpperCase(Locale.ROOT) + s.substring(1);
    }
}
