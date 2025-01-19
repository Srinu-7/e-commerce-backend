package com.zosh.e_commerce.ServiceImplementation;

import com.zosh.e_commerce.Exception.CartItemNotFoundException;
import com.zosh.e_commerce.Exception.UserNotFoundException;
import com.zosh.e_commerce.Model.Cart;
import com.zosh.e_commerce.Model.CartItem;
import com.zosh.e_commerce.Model.Product;
import com.zosh.e_commerce.Model.User;
import com.zosh.e_commerce.Repository.CartItemRepository;
import com.zosh.e_commerce.Repository.CartRepository;
import com.zosh.e_commerce.Repository.UserRepository;
import com.zosh.e_commerce.ServiceInterface.CartItemService;
import com.zosh.e_commerce.ServiceInterface.UserService;
import org.springframework.stereotype.Service;

@Service
public class CartItemServiceImplementation implements CartItemService {

    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final UserService userService;

    public CartItemServiceImplementation(CartItemRepository cartItemRepository, UserRepository userRepository, CartRepository cartRepository,UserService userService) {
        this.cartItemRepository = cartItemRepository;
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
        this.userService = userService;
    }

    @Override
    public CartItem updateCartItem(Long userId, Long cartItemId, CartItem cartItem) throws CartItemNotFoundException, UserNotFoundException {

        CartItem existingCartItem = findCartItemById(cartItemId);

        User user = userService.findUserById(existingCartItem.getUserId());

        if(user.getId().equals(userId)) {

            existingCartItem.setQuantity(cartItem.getQuantity());

            existingCartItem.setPrice(existingCartItem.getProduct().getPrice() * existingCartItem.getQuantity());

            existingCartItem.setDiscountedPrice(existingCartItem.getProduct().getDiscountedPrice() * existingCartItem.getQuantity());

        }

        return cartItemRepository.save(existingCartItem);
    }

    @Override
    public CartItem findCartItemById(Long cartItemId) throws CartItemNotFoundException {
        return cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new CartItemNotFoundException("CartItem not found with id " + cartItemId));
    }

    @Override
    public void deleteCartItem(Long userId, Long cartItemId) throws CartItemNotFoundException, UserNotFoundException {
        // Fetch the cart item
        CartItem cartItem = findCartItemById(cartItemId);

        // Verify that the user is authorized
        if (!cartItem.getUserId().equals(userId)) {
            throw new UserNotFoundException("User is not authorized to remove this cart item.");
        }

        // Delete the cart item
        cartItemRepository.deleteById(cartItemId);
    }

    @Override
    public CartItem isCartItemExist(Cart cart, Product product, String size, Long userId){

        CartItem cartItem = cartItemRepository.findCartItem(cart.getId(), product.getId(), size, userId);

        return cartItem;

    }

    @Override
    public CartItem createCartItem(CartItem cartItem){

        cartItem.setQuantity(1);

        cartItem.setPrice(cartItem.getProduct().getPrice() * cartItem.getQuantity());

        cartItem.setDiscountedPrice(cartItem.getProduct().getDiscountedPrice() * cartItem.getQuantity());

        CartItem savedCartItem = cartItemRepository.save(cartItem);

        return savedCartItem;
    }
}
