package com.zosh.e_commerce.ServiceImplementation;

import com.zosh.e_commerce.Exception.CartItemNotFoundException;
import com.zosh.e_commerce.Exception.ProductNotFoundException;
import com.zosh.e_commerce.Exception.UserNotFoundException;
import com.zosh.e_commerce.Model.Cart;
import com.zosh.e_commerce.Model.CartItem;
import com.zosh.e_commerce.Model.Product;
import com.zosh.e_commerce.Model.User;
import com.zosh.e_commerce.Repository.CartItemRepository;
import com.zosh.e_commerce.Repository.CartRepository;
import com.zosh.e_commerce.Repository.ProductRepository;
import com.zosh.e_commerce.Request.CartItemRequest;
import com.zosh.e_commerce.ServiceInterface.CartService;
import com.zosh.e_commerce.ServiceInterface.ProductService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartServiceImplementation implements CartService {

    private final CartRepository cartRepository;
    private final ProductService productService;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    public CartServiceImplementation(
            CartRepository cartRepository,
            ProductService productService,
            CartItemRepository cartItemRepository,
            ProductRepository productRepository
    ) {
        this.cartRepository = cartRepository;
        this.productService = productService;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
    }

    @Override
    @Transactional
    public Cart findUserCart(Long userId) {

        Cart cart = cartRepository.findByUserId(userId);

        int totalPrice = 0;
        int totalQuantity = 0;
        int totalDiscountedPrice = 0;

        List<CartItem> items = new ArrayList<>(cart.getCartItems());

        for (CartItem item : items) {

            totalPrice = totalPrice + item.getPrice();

            totalQuantity = totalQuantity + item.getQuantity();

            totalDiscountedPrice = totalDiscountedPrice + item.getDiscountedPrice();
        }

        cart.setTotalPrice(totalPrice);
        cart.setTotalQuantity(totalQuantity);
        cart.setTotalDiscountedPrice(totalDiscountedPrice);
        cart.setDiscount((int)totalPrice-totalDiscountedPrice);

        return cartRepository.save(cart);
    }

    @Override
    public Cart createCart(User user) {
        Cart cart = new Cart();
        cart.setUser(user);
        return cartRepository.save(cart);
    }

    @Override
    @Transactional
    public String addItemToCart(Long userId, CartItemRequest cartItemRequest)
            throws ProductNotFoundException, CartItemNotFoundException, UserNotFoundException {
        Cart cart = cartRepository.findByUserId(userId);
        Product product = productService.findProductById(cartItemRequest.getProductId());

        CartItem existingCartItem = isCartItemExist(cart, product, cartItemRequest.getSize(), userId);

        if (existingCartItem == null) {
            CartItem cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setCart(cart);
            cartItem.setQuantity(1);
            cartItem.setUserId(userId);
            cartItem.setSize(cartItemRequest.getSize());
            cartItem.setDiscountedPrice(product.getDiscountedPrice());
            cartItem.setPrice(product.getPrice());
            CartItem savedItem = cartItemRepository.save(cartItem);
            cart.getCartItems().add(savedItem);
        }
        else{
            existingCartItem.setQuantity(existingCartItem.getQuantity() + 1);
            existingCartItem.setDiscountedPrice(existingCartItem.getQuantity() * product.getDiscountedPrice());
            existingCartItem.setPrice(existingCartItem.getQuantity() * product.getPrice());
            cartItemRepository.save(existingCartItem);
        }

        return "item added succesfully";
    }

    @Override
    public CartItem deleteCartItem(Long userId, Long cartItemId) throws CartItemNotFoundException, UserNotFoundException {

        Cart cart = cartRepository.findByUserId(userId);

        CartItem cartItem = cartItemRepository.findById(cartItemId).get();

        Product product = productRepository.findById(cartItem.getProduct().getId()).get();

        if (!cartItem.getUserId().equals(userId)) throw new UserNotFoundException("User is not authorized to remove this cart item.");

        cart.getCartItems().remove(cartItem);

        cartItemRepository.delete(cartItem);

        return cartItem;
    }

    @Override
    @Transactional
    public CartItem updateCartItem(Long cartItemId,int quantity, Long userId) throws CartItemNotFoundException {
        Cart cart = cartRepository.findByUserId(userId);
        CartItem cartItem = findCartItemByCartItemId(cartItemId);
        cartItem.setQuantity(quantity);
        cartItem.setDiscountedPrice(quantity * cartItem.getProduct().getDiscountedPrice());
        cartItem.setPrice(quantity * cartItem.getProduct().getPrice());
        return cartItemRepository.save(cartItem);
    }

    @Override
    public CartItem isCartItemExist(Cart cart, Product product, String size, Long userId){
        return cartItemRepository.findCartItem(cart.getId(), product.getId(), size, userId);
    }


    @Override
    public CartItem findCartItemByCartItemId(Long cartItemId) throws CartItemNotFoundException {

        return cartItemRepository.findById(cartItemId).get();

    }
}
