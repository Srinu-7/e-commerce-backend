package com.zosh.e_commerce.ServiceInterface;

import com.zosh.e_commerce.Exception.CartItemNotFoundException;
import com.zosh.e_commerce.Exception.ProductNotFoundException;
import com.zosh.e_commerce.Exception.UserNotFoundException;
import com.zosh.e_commerce.Model.Cart;
import com.zosh.e_commerce.Model.User;
import com.zosh.e_commerce.Request.CartItemRequest;
import org.springframework.stereotype.Service;

@Service
public interface CartService {

    public Cart createCart(User user);

    public String addItemToCart(Long userId, CartItemRequest cartItemRequest) throws ProductNotFoundException, CartItemNotFoundException, UserNotFoundException;

    public Cart findUserCart(Long userId);

}
