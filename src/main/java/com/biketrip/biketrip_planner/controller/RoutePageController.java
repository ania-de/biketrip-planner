package com.biketrip.biketrip_planner.controller;

import com.biketrip.biketrip_planner.classes.Difficulty;
import com.biketrip.biketrip_planner.classes.Review;
import com.biketrip.biketrip_planner.service.ReviewService;
import com.biketrip.biketrip_planner.weather.WeatherDTO;
import com.biketrip.biketrip_planner.weather.WeatherService;
import org.springframework.ui.Model;
import com.biketrip.biketrip_planner.classes.Route;
import com.biketrip.biketrip_planner.dto.RouteForm;
import com.biketrip.biketrip_planner.service.CategoryService;
import com.biketrip.biketrip_planner.service.RouteService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
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

    public RoutePageController(RouteService routes,
                               CategoryService categories,
                               ReviewService reviews,
                               WeatherService weather) {
        this.routes = routes;
        this.categories = categories;
        this.reviews = reviews;
        this.weather = weather;
    }


    @GetMapping
    public String list(@RequestParam(required=false) String city, Model model) {
        List<Route> data = (city == null || city.isBlank()) ? routes.findAll() : routes.findByCity(city);
        model.addAttribute("routes", data);
        model.addAttribute("city", city == null ? "" : city);
        model.addAttribute("form", new RouteForm());
        model.addAttribute("categories", categories.findAll());
        model.addAttribute("difficulties", Difficulty.values());
        return "routes/list";
    }

    @PostMapping
    public String create(@ModelAttribute("form") @Valid RouteForm form,
                         BindingResult br, RedirectAttributes ra, Model model) {
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
        r.setDistance(form.getDistance());         // km
        r.setDuration(form.getDurationMin());      // min
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
        updateForm.setDurationMin(r.getDuration()); // w encji trzymasz minuty

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
                         BindingResult br, RedirectAttributes ra, Model model) {
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
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
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
                            RedirectAttributes ra) {
        Route r = routes.findById(id).orElseThrow();
        var review = new com.biketrip.biketrip_planner.classes.Review();
        review.setRoute(r);
        review.setRating(rating);
        review.setContent(content);
        reviews.createReview(review);
        ra.addFlashAttribute("ok", "Dodano opinię");
        return "redirect:/ui/routes/" + id;
    }
}