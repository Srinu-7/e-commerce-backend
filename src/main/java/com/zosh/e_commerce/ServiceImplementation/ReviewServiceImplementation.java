package com.zosh.e_commerce.ServiceImplementation;

import com.zosh.e_commerce.Exception.ProductNotFoundException;
import com.zosh.e_commerce.Model.Product;
import com.zosh.e_commerce.Model.Review;
import com.zosh.e_commerce.Model.User;
import com.zosh.e_commerce.Repository.ProductRepository;
import com.zosh.e_commerce.Repository.ReviewRepository;
import com.zosh.e_commerce.Request.ReviewRequest;
import com.zosh.e_commerce.ServiceInterface.ProductService;
import com.zosh.e_commerce.ServiceInterface.ReviewService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReviewServiceImplementation implements ReviewService {

    private final ReviewRepository reviewRepository;

    private final ProductService productService;

    private final ProductRepository productRepository;

    public ReviewServiceImplementation(ReviewRepository reviewRepository, ProductService productService,ProductRepository productRepository) {

        this.reviewRepository = reviewRepository;

        this.productService = productService;

        this.productRepository = productRepository;
    }

    @Override
    public Review createReview(User user, ReviewRequest reviewRequest) throws ProductNotFoundException {

        Product product = productService.findProductById(reviewRequest.getProductId());

        Review review = new Review();

        review.setProduct(product);

        review.setUser(user);

        review.setReview(reviewRequest.getReview());

        review.setCreatedAt(LocalDateTime.now());

        product.getReviews().add(review);

        return reviewRepository.save(review);
    }

    @Override
    public List<Review> getReviews(Long productId) {

        return reviewRepository.findByProductId(productId);

    }
}
