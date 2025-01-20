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
        return cartRepository.findByUserId(userId);
    }

    @Override
    public Cart createCart(User user) {
        Cart cart = new Cart();
        cart.setUser(user);
        cart.setDiscount(0);
        cart.setTotalPrice(0.0);  // Make sure to initialize totalPrice as a double
        cart.setTotalQuantity(0);
        cart.setTotalDiscountedPrice(0); // Keep totalDiscountedPrice as int
        return cartRepository.save(cart);
    }

    @Override
    @Transactional
    public String addItemToCart(Long userId, CartItemRequest cartItemRequest)
            throws ProductNotFoundException, CartItemNotFoundException, UserNotFoundException {
        Cart cart = findUserCart(userId);
        Product product = productService.findProductById(cartItemRequest.getProductId());

        CartItem existingCartItem = isCartItemExist(cart, product, cartItemRequest.getSize(), userId);

        if (existingCartItem == null) {
            if(cartItemRequest.getQuantity() <= 0) throw new ProductNotFoundException("product has no quantity");
            CartItem cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setCart(cart);
            cartItem.setQuantity(cartItemRequest.getQuantity());
            cartItem.setUserId(userId);
            cartItem.setSize(cartItemRequest.getSize());
            cartItem.setDiscountedPrice(cartItem.getQuantity() * product.getDiscountedPrice());
            cartItem.setPrice(cartItem.getQuantity() * product.getPrice());

            cart.setTotalPrice(cart.getTotalPrice() + cartItem.getQuantity() * product.getPrice());
            cart.setTotalQuantity(cart.getTotalQuantity() + cartItem.getQuantity());
            cart.setTotalDiscountedPrice(cart.getTotalDiscountedPrice() + cartItem.getQuantity() * product.getDiscountedPrice());
            cart.setDiscount((int)cart.getTotalPrice()-cart.getTotalDiscountedPrice());

            cartRepository.save(cart);
            cartItemRepository.save(cartItem);
            cart.getItems().add(cartItem);
        } else {
            if (cartItemRequest.getQuantity() > 0) {
                addQuantityToCartItem(cartItemRequest, userId);
            } else if(cartItemRequest.getQuantity() < 0){
                reduceQuantityFromCartItem(cartItemRequest, userId);
            }
        }

        return "Cart updated successfully.";
    }

    @Override
    public void deleteCartItem(Long userId, Long cartItemId) throws CartItemNotFoundException, UserNotFoundException {

        Cart cart = findUserCart(userId);

        CartItem cartItem = cartItemRepository.findById(cartItemId).get();

        Product product = productRepository.findById(cartItem.getProduct().getId()).get();

        if (!cartItem.getUserId().equals(userId)) throw new UserNotFoundException("User is not authorized to remove this cart item.");

        int totalQuantity = cartItem.getQuantity();
        int totalDiscountedPrice = totalQuantity * product.getDiscountedPrice();
        int totalPrice = totalQuantity * product.getPrice();

        cart.setTotalQuantity(cart.getTotalQuantity() -totalQuantity);
        cart.setTotalPrice(cart.getTotalPrice()-totalPrice);
        cart.setTotalDiscountedPrice(cart.getTotalDiscountedPrice()-totalDiscountedPrice);
        cart.setDiscount((int)cart.getTotalPrice()-cart.getTotalDiscountedPrice());

        cart.getItems().remove(cartItem);
        cartItemRepository.delete(cartItem);
        cartRepository.save(cart);
    }

    @Override
    @Transactional
    public CartItem addQuantityToCartItem(CartItemRequest cartItemRequest, Long userId) throws CartItemNotFoundException {
        Cart cart = findUserCart(userId);
        Product product = productRepository.findById(cartItemRequest.getProductId()).get();

        CartItem cartItem = isCartItemExist(cart, product, cartItemRequest.getSize(), userId);

        int totalQuantity = Math.min(cartItem.getQuantity() + cartItemRequest.getQuantity(),product.getQuantity());
        int totalDiscountedPrice = totalQuantity * product.getDiscountedPrice();
        int totalPrice = totalQuantity * product.getPrice();

        int QuantityToadd = totalQuantity-cartItem.getQuantity();

        cartItem.setQuantity(totalQuantity);
        cartItem.setDiscountedPrice(totalDiscountedPrice);
        cartItem.setPrice(totalPrice);

        cart.setTotalQuantity(cart.getTotalQuantity() + QuantityToadd);
        cart.setTotalPrice(cart.getTotalPrice()+ QuantityToadd*product.getPrice());
        cart.setTotalDiscountedPrice(cart.getTotalDiscountedPrice()+ QuantityToadd*product.getDiscountedPrice());
        cart.setDiscount((int)cart.getTotalPrice()-cart.getTotalDiscountedPrice());

        cartRepository.save(cart);

        return cartItemRepository.save(cartItem);
    }

    @Override
    @Transactional
    public CartItem reduceQuantityFromCartItem(CartItemRequest cartItemRequest, Long userId) throws CartItemNotFoundException {
        Cart cart = findUserCart(userId);
        Product product = productRepository.findById(cartItemRequest.getProductId()).get();

        CartItem cartItem = isCartItemExist(cart, product, cartItemRequest.getSize(), userId);

        int totalQuantity = Math.max(cartItem.getQuantity() + cartItemRequest.getQuantity(),0);
        int totalDiscountedPrice = totalQuantity * product.getDiscountedPrice();
        int totalPrice = totalQuantity * product.getPrice();

        int quantityToSubstract = cartItem.getQuantity()-totalQuantity;

        cartItem.setQuantity(totalQuantity);
        cartItem.setDiscountedPrice(totalDiscountedPrice);
        cartItem.setPrice(totalPrice);

        cart.setTotalQuantity(cart.getTotalQuantity() - quantityToSubstract);
        cart.setTotalPrice(cart.getTotalPrice()- quantityToSubstract*product.getPrice());
        cart.setTotalDiscountedPrice(cart.getTotalDiscountedPrice()- quantityToSubstract*product.getDiscountedPrice());
        cart.setDiscount((int)cart.getTotalPrice()-cart.getTotalDiscountedPrice());

        cartRepository.save(cart);

        if (totalQuantity == 0) {
            cart.getItems().remove(cartItem);
            cartItemRepository.delete(cartItem);
            return cartItem;
        }

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
