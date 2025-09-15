package com.biketrip.biketrip_planner.controller;

import com.biketrip.biketrip_planner.classes.Difficulty;
import com.biketrip.biketrip_planner.classes.Point;
import com.biketrip.biketrip_planner.classes.Review;
import com.biketrip.biketrip_planner.classes.Route;
import com.biketrip.biketrip_planner.dto.RouteForm;
import com.biketrip.biketrip_planner.service.*;
import com.biketrip.biketrip_planner.weather.WeatherDTO;
import com.biketrip.biketrip_planner.weather.WeatherService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/ui/routes")
public class RoutePageController {
    private final RouteService routes;
    private final CategoryService categories;
    private final ReviewService reviews;
    private final WeatherService weather;
    private final PointService pointService;
    private final ExternalRouteService externalRoute;

    public RoutePageController(RouteService routes,
                               CategoryService categories,
                               ReviewService reviews,
                               WeatherService weather,
                               PointService pointService,
                               ExternalRouteService externalRoute ) {
        this.routes = routes;
        this.categories = categories;
        this.reviews = reviews;
        this.weather = weather;
        this.pointService = pointService;
        this.externalRoute = externalRoute;
    }


    @GetMapping
    public String list(@RequestParam(required = false) String city,
                       @RequestParam(required = false) Difficulty difficulty,
                       @RequestParam(required = false) Double minDistance,   // km
                       @RequestParam(required = false) Double maxDistance,   // km
                       @RequestParam(required = false) Double maxDuration,   // min
                       Model model) {

        boolean noFilters =
                (city == null || city.isBlank()) &&
                        difficulty == null &&
                        minDistance == null && maxDistance == null &&
                        maxDuration == null;

        var data = noFilters
                ? routes.findAll()
                : routes.searchSimple(city, difficulty, minDistance, maxDistance, maxDuration);

        model.addAttribute("routes", data);
        model.addAttribute("city", city == null ? "" : city);
        model.addAttribute("difficulties", Difficulty.values());
        model.addAttribute("form", new RouteForm());
        model.addAttribute("categories", categories.findAll());
        return "routes/list";
    }

    @PostMapping
    public String create(@ModelAttribute("form") @Valid RouteForm form,
                         BindingResult br,
                         RedirectAttributes ra,
                         Model model,
                         HttpSession session) {
        if (session.getAttribute("uid") == null) {
            return "redirect:/ui/auth/login?next=/ui/routes";
        }
        if (br.hasErrors()) {
            model.addAttribute("routes", routes.findAll());
            model.addAttribute("categories", categories.findAll());
            model.addAttribute("difficulties", Difficulty.values());
            return "routes/list";
        }
        Route r = new Route();
        r.setName(form.getName());
        r.setCity(form.getCity());
        r.setCountry("Poland");
        r.setDifficulty(form.getDifficulty());
        r.setDistance(form.getDistance());
        r.setDuration(form.getDurationMin());
        if (form.getCategoryId() != null) {
            categories.findById(form.getCategoryId()).ifPresent(r::setCategory);
        }
        routes.createRoute(r);
        ra.addFlashAttribute("ok", "Utworzono trasę");
        return "redirect:/ui/routes";
    }


    @GetMapping("/{id}")
    public String details(@PathVariable Long id,
                          @RequestParam(defaultValue = "true") boolean includeWeather,
                          Model model) {
        Route r = routes.findById(id).orElseThrow();
        double avg = reviews.calculateAverageRating(id);
        List<Review> reviewList = reviews.findReviewsByRouteId(id);

        WeatherDTO w = null;
        if (includeWeather) {
            try { w = weather.getCityWeather(r.getCity()); } catch (Exception ignored) {}
        }

        RouteForm updateForm = new RouteForm();
        updateForm.setName(r.getName());
        updateForm.setCity(r.getCity());
        updateForm.setDifficulty(r.getDifficulty());
        updateForm.setDistance(r.getDistance());
        updateForm.setDurationMin(r.getDuration());

        var points = pointService.findPointsByRouteIdOrdered(id);
        model.addAttribute("points", points);

        model.addAttribute("route", r);
        model.addAttribute("avg", avg);
        model.addAttribute("reviewList", reviewList);
        model.addAttribute("weather", w);
        model.addAttribute("updateForm", updateForm);
        model.addAttribute("categories", categories.findAll());
        model.addAttribute("difficulties", Difficulty.values());

        return "routes/details";
    }


    @PostMapping("/{id}/update")
    public String update(@PathVariable Long id,
                         @ModelAttribute("updateForm") @Valid RouteForm form,
                         BindingResult br,
                         RedirectAttributes ra,
                         Model model,
                         HttpSession session) {
        if (session.getAttribute("uid") == null) {
            return "redirect:/ui/auth/login?next=/ui/routes/" + id;
        }
        Route r = routes.findById(id).orElseThrow();

        if (br.hasErrors()) {
            return details(id, false, model);
        }
        r.setName(form.getName());
        r.setCity(form.getCity());
        r.setCountry("Poland");
        r.setDifficulty(form.getDifficulty());
        r.setDistance(form.getDistance());
        r.setDuration(form.getDurationMin());
        if (form.getCategoryId() != null) {
            categories.findById(form.getCategoryId()).ifPresent(r::setCategory);
        }
        routes.save(r);
        ra.addFlashAttribute("ok", "Zapisano zmiany");
        return "redirect:/ui/routes/" + id;
    }


    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id,
                         RedirectAttributes ra,
                         HttpSession session) {
        if (session.getAttribute("uid") == null) {
            return "redirect:/ui/auth/login?next=/ui/routes";
        }
        routes.deleteById(id);
        ra.addFlashAttribute("ok", "Usunięto trasę #" + id);
        return "redirect:/ui/routes";
    }


    @PostMapping("/{id}/calories")
    public String calories(@PathVariable Long id,
                           @RequestParam double weightKg,
                           RedirectAttributes ra) {
        double kcal = routes.calculateCalories(id, weightKg);
        ra.addFlashAttribute("kcal", kcal);
        ra.addFlashAttribute("lastWeight", weightKg);
        return "redirect:/ui/routes/" + id;
    }


    @PostMapping("/{id}/reviews")
    public String addReview(@PathVariable Long id,
                            @RequestParam int rating,
                            @RequestParam String content,
                            RedirectAttributes ra,
                            HttpSession session) {
        if (session.getAttribute("uid") == null) {
            return "redirect:/ui/auth/login?next=/ui/routes/" + id;
        }
        Route r = routes.findById(id).orElseThrow();
        Review review = new Review();
        review.setRoute(r);
        review.setRating(rating);
        review.setContent(content);
        reviews.createReview(review);
        ra.addFlashAttribute("ok", "Dodano opinię");
        return "redirect:/ui/routes/" + id;
    }
    @PostMapping("/{id}/points")
    public String addPoint(@PathVariable Long id,
                           @RequestParam String name,
                           @RequestParam("lat") double lat,
                           @RequestParam("lon") double lon,
                           RedirectAttributes ra) {

        var route = routes.findById(id).orElseThrow();

        if (lat < -90 || lat > 90 || lon < -180 || lon > 180) {
            ra.addFlashAttribute("err", "Nieprawidłowe współrzędne");
            return "redirect:/ui/routes/" + id;
        }

        var p = new Point();
        p.setRoute(route);
        p.setName(name);
        p.setLatitude(lat);
        p.setLongitude(lon);


    pointService.addPoint(p);


        ra.addFlashAttribute("ok", "Dodano punkt");
        return "redirect:/ui/routes/" + id;
    }
    @GetMapping("/{id}/points")
    public String pointsGet(@PathVariable Long id) {
        return "redirect:/ui/routes/" + id;
    }


    @GetMapping("/{id}/delete")
    public String deleteGet(@PathVariable Long id) {
        return "redirect:/ui/routes";
    }


    @GetMapping("/generate")
    public String generateGet() {
        return "redirect:/ui/routes";
    }
    @PostMapping("/generate")
    public String generate(@RequestParam String city,
                           @RequestParam double targetKm,
                           jakarta.servlet.http.HttpSession session,
                           org.springframework.web.servlet.mvc.support.RedirectAttributes ra) {
        if (session.getAttribute("uid") == null) {
            return "redirect:/ui/auth/login?next=/ui/routes";
        }
        try {
            var r = externalRoute.generateRoundTrip(city.trim(), targetKm);
            ra.addFlashAttribute("ok", "Wygenerowano trasę: " + r.getName());
            return "redirect:/ui/routes/" + r.getId();
        } catch (Exception e) {
            ra.addFlashAttribute("err", "Nie udało się wygenerować trasy: " + e.getMessage());
            return "redirect:/ui/routes";
        }
    }
}
