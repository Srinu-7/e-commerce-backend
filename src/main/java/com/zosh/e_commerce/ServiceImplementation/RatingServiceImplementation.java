package com.zosh.e_commerce.ServiceImplementation;

import com.zosh.e_commerce.Exception.ProductNotFoundException;
import com.zosh.e_commerce.Model.Product;
import com.zosh.e_commerce.Model.Rating;
import com.zosh.e_commerce.Model.User;
import com.zosh.e_commerce.Repository.ProductRepository;
import com.zosh.e_commerce.Repository.RatingRepository;
import com.zosh.e_commerce.Request.RatingRequest;
import com.zosh.e_commerce.ServiceInterface.ProductService;
import com.zosh.e_commerce.ServiceInterface.RatingService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service

public class RatingServiceImplementation implements RatingService {

    private final RatingRepository ratingRepository;

    private final ProductService productService;

    private final ProductRepository productRepository;

    public RatingServiceImplementation(RatingRepository ratingRepository, ProductService productService,ProductRepository productRepository) {

        this.ratingRepository = ratingRepository;

        this.productService = productService;

        this.productRepository = productRepository;

    }

    @Override
    public Rating createRating(User user, RatingRequest ratingRequest) throws ProductNotFoundException {

        Product product = productService.findProductById(ratingRequest.getProductId());

        Rating rating = new Rating();

        rating.setProduct(product);

        rating.setUser(user);

        rating.setRating(ratingRequest.getRating());

        rating.setCreatedAt(LocalDateTime.now());

        product.getRatings().add(rating);

        return ratingRepository.save(rating);
    }

    @Override
    public List<Rating> getProductsRatings(Long productId) {

        List<Rating> ratings = ratingRepository.findByProductId(productId);

        return ratings;
    }
}
