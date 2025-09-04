package com.biketrip.biketrip_planner.service;

import com.biketrip.biketrip_planner.classes.Review;
import com.biketrip.biketrip_planner.classes.Route;
import com.biketrip.biketrip_planner.repository.ReviewRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class ReviewService {
    private final ReviewRepository reviewRepository;

    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }
    public Review createReview(Review review) {
        return reviewRepository.save(review);
    }
    public Review findReviewsByRouteId(Long routeId) {
        return reviewRepository.findById(routeId).orElse(null);
    }
    public List<Review> findByRoute(Route route) {
        return reviewRepository.findByRoute(route);
    }
    public double calculateAverageRating(Long routeId) {
        List <Review> reviews = reviewRepository.findByRouteId(routeId);
        if (reviews.isEmpty()) return 0.0;
        return reviews.stream().mapToDouble(Review::getRating).average().orElse(0.0);
        }

    }


