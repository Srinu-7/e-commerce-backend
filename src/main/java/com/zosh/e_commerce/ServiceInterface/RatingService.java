package com.zosh.e_commerce.ServiceInterface;

import com.zosh.e_commerce.Exception.ProductNotFoundException;
import com.zosh.e_commerce.Model.Rating;
import com.zosh.e_commerce.Model.User;
import com.zosh.e_commerce.Request.RatingRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RatingService {

    public Rating createRating(User user, RatingRequest ratingRequest) throws ProductNotFoundException;

    public List<Rating> getProductsRatings(Long productId);
}
