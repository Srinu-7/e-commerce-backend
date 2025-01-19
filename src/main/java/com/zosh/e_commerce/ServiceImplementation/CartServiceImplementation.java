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
import com.zosh.e_commerce.Request.CartItemRequest;
import com.zosh.e_commerce.ServiceInterface.CartItemService;
import com.zosh.e_commerce.ServiceInterface.CartService;
import com.zosh.e_commerce.ServiceInterface.ProductService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class CartServiceImplementation implements CartService {

    private final CartRepository cartRepository;
    private final CartItemService cartItemService;
    private final ProductService productService;
    private final CartItemRepository cartItemRepository;

    public CartServiceImplementation(CartRepository cartRepository, CartItemService cartItemService, ProductService productService,CartItemRepository cartItemRepository) {
        this.cartRepository = cartRepository;
        this.cartItemService = cartItemService;
        this.productService = productService;
        this.cartItemRepository = cartItemRepository;
    }

    @Override
    @Transactional
    public Cart findUserCart(Long userId) {
        Cart cart = cartRepository.findByUserId(userId);

        // Use streams for more concise calculation of totals
        int totalPrice = cart.getItems().stream().mapToInt(CartItem::getPrice).sum();
        int totalDiscountedPrice = cart.getItems().stream().mapToInt(CartItem::getDiscountedPrice).sum();
        int totalQuantity = cart.getItems().stream().mapToInt(CartItem::getQuantity).sum();

        cart.setTotalPrice(totalPrice);
        cart.setTotalDiscountedPrice(totalDiscountedPrice);
        cart.setTotalQuantity(totalQuantity);
        cart.setDiscount(totalPrice - totalDiscountedPrice);

        return cartRepository.save(cart);  // Ensure cart is saved after totals calculation
    }

    @Override
    public Cart createCart(User user) {
        Cart cart = new Cart();
        cart.setUser(user);
        return cartRepository.save(cart);
    }

    @Override
    @Transactional
    public String addItemToCart(Long userId, CartItemRequest cartItemRequest) throws ProductNotFoundException, CartItemNotFoundException, UserNotFoundException {
        Cart cart = cartRepository.findByUserId(userId);

        // Handle non-existent product
        Product product = productService.findProductById(cartItemRequest.getProductId());

        // Check if the item already exists in the cart
        CartItem existingCartItem = cartItemService.isCartItemExist(cart, product, cartItemRequest.getSize(), userId);

        if (existingCartItem == null) {
            CartItem cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setCart(cart);
            cartItem.setQuantity(cartItemRequest.getQuantity());
            cartItem.setUserId(userId);
            cartItem.setSize(cartItemRequest.getSize());
            cartItem.setDiscountedPrice(cartItem.getQuantity() * product.getDiscountedPrice());
            cartItem.setPrice(cartItem.getQuantity() * product.getPrice());
            CartItem savedItem = cartItemRepository.save(cartItem);
            cart.getItems().add(savedItem);

        } else {
            existingCartItem.setQuantity(existingCartItem.getQuantity() + cartItemRequest.getQuantity());
            existingCartItem.setDiscountedPrice(existingCartItem.getQuantity() * product.getDiscountedPrice());
            existingCartItem.setPrice(existingCartItem.getQuantity() * product.getPrice());
        }

        cartRepository.save(cart);

        return "Item Added to Cart";
    }
}
