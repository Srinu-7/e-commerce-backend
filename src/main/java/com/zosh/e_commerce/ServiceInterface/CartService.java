package com.zosh.e_commerce.ServiceInterface;

import com.zosh.e_commerce.Exception.CartItemNotFoundException;
import com.zosh.e_commerce.Exception.ProductNotFoundException;
import com.zosh.e_commerce.Exception.UserNotFoundException;
import com.zosh.e_commerce.Model.Cart;
import com.zosh.e_commerce.Model.CartItem;
import com.zosh.e_commerce.Model.Product;
import com.zosh.e_commerce.Model.User;
import com.zosh.e_commerce.Request.CartItemRequest;
import org.springframework.stereotype.Service;

@Service
public interface CartService {

    // Find the cart for a specific user
    Cart findUserCart(Long userId);

    // Create a new cart for a user
    Cart createCart(User user);

    // Add an item to the user's cart, updating quantities or creating new items as necessary
    String addItemToCart(Long userId, CartItemRequest cartItemRequest)
            throws ProductNotFoundException, CartItemNotFoundException, UserNotFoundException;

    // Delete an item from the user's cart
    CartItem deleteCartItem(Long userId, Long cartItemId) throws CartItemNotFoundException, UserNotFoundException;

    // Reduce quantity of an existing cart item
    CartItem updateCartItem(Long cartItemId,int quantity, Long userId) throws CartItemNotFoundException;

    // Check if the cart item already exists in the cart
    CartItem isCartItemExist(Cart cart, Product product, String size, Long userId);

    CartItem findCartItemByCartItemId(Long cartItemId) throws CartItemNotFoundException;
}
