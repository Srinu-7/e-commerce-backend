package com.zosh.e_commerce.ServiceInterface;

import com.zosh.e_commerce.Exception.ProductNotFoundException;
import com.zosh.e_commerce.Model.Review;
import com.zosh.e_commerce.Model.User;
import com.zosh.e_commerce.Request.ReviewRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ReviewService {

    public Review createReview(User user, ReviewRequest reviewRequest) throws ProductNotFoundException;

    public List<Review> getReviews(Long productId);
}
