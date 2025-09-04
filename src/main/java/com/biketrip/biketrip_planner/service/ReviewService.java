package com.biketrip.biketrip_planner.service;

import com.biketrip.biketrip_planner.classes.Review;
import com.biketrip.biketrip_planner.classes.Route;
import com.biketrip.biketrip_planner.repository.ReviewRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ReviewService {
    private final ReviewRepository reviewRepository;

    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public Review createReview(@Valid Review review) {
        return reviewRepository.save(review);
    }

    public List<Review> findReviewsByRouteId(Long routeId) {
        return reviewRepository.findByRouteId(routeId);
    }

    public List<Review> findByRoute(Route route) {
        return reviewRepository.findByRoute(route);
    }

    public double calculateAverageRating(Long routeId) {
        Double avg = reviewRepository.avgRatingForRoute(routeId);
        return avg != null ? Math.round(avg * 10.0) / 10.0 : 0.0;
    }
    public Optional<Review> findById(Long id) { return reviewRepository.findById(id); }
    public void deleteById(Long id) { reviewRepository.deleteById(id); }

}


