package com.zosh.e_commerce.Controller;

import com.zosh.e_commerce.Exception.ProductNotFoundException;
import com.zosh.e_commerce.Exception.UserNotFoundException;
import com.zosh.e_commerce.Model.Review;
import com.zosh.e_commerce.Model.User;
import com.zosh.e_commerce.Request.ReviewRequest;
import com.zosh.e_commerce.ServiceInterface.ReviewService;
import com.zosh.e_commerce.ServiceInterface.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    private final UserService userService;

    public ReviewController(ReviewService reviewService, UserService userService) {

        this.reviewService = reviewService;

        this.userService = userService;
    }

    @PostMapping("/create")
    public ResponseEntity<Review> createReview(@RequestBody ReviewRequest reviewRequest, @RequestHeader("Authorization") String jwt) throws UserNotFoundException, ProductNotFoundException {

        User user = userService.findUserProfileByJwt(jwt);

        Review review = reviewService.createReview(user, reviewRequest);

        return new ResponseEntity<>(review, HttpStatus.CREATED);
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<Review>> getProductsReviews(@PathVariable Long productId) throws UserNotFoundException, ProductNotFoundException {

        List<Review> reviews = reviewService.getReviews(productId);

        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }

}
