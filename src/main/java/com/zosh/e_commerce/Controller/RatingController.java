package com.zosh.e_commerce.Controller;

import com.zosh.e_commerce.Exception.ProductNotFoundException;
import com.zosh.e_commerce.Exception.UserNotFoundException;
import com.zosh.e_commerce.Model.Rating;
import com.zosh.e_commerce.Model.User;
import com.zosh.e_commerce.Request.RatingRequest;
import com.zosh.e_commerce.ServiceInterface.RatingService;
import com.zosh.e_commerce.ServiceInterface.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ratings")
public class RatingController {

    private final RatingService ratingService;

    private final UserService userService;

    public RatingController(RatingService ratingService, UserService userService) {

        this.ratingService = ratingService;

        this.userService = userService;

    }

    @PostMapping("/create")
    public ResponseEntity<Rating> createRating(@RequestBody RatingRequest ratingRequest, @RequestHeader("Authorization") String jwt) throws UserNotFoundException, ProductNotFoundException {

        User user = userService.findUserProfileByJwt(jwt);

        Rating rating =ratingService.createRating(user, ratingRequest);

        return new ResponseEntity<>(rating, HttpStatus.CREATED);
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<Rating>>getProductsRating(@RequestHeader("Authorization") String jwt,@PathVariable Long productId) throws UserNotFoundException, ProductNotFoundException {

        User user = userService.findUserProfileByJwt(jwt);

        List<Rating> ratings = ratingService.getProductsRatings(productId);

        return new ResponseEntity<>(ratings, HttpStatus.OK);
    }
}
