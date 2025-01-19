package com.zosh.e_commerce.Controller;

import com.zosh.e_commerce.Exception.CartItemNotFoundException;
import com.zosh.e_commerce.Exception.ProductNotFoundException;
import com.zosh.e_commerce.Exception.UserNotFoundException;
import com.zosh.e_commerce.Model.Cart;
import com.zosh.e_commerce.Model.CartItem;
import com.zosh.e_commerce.Model.Product;
import com.zosh.e_commerce.Model.User;
import com.zosh.e_commerce.Request.CartItemRequest;
import com.zosh.e_commerce.ServiceInterface.CartItemService;
import com.zosh.e_commerce.ServiceInterface.CartService;
import com.zosh.e_commerce.ServiceInterface.ProductService;
import com.zosh.e_commerce.ServiceInterface.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart_items")
public class CartItemController {

    private final CartItemService cartItemService;

    private final UserService userService;

    private final CartService cartService;

    private final ProductService productService;

    public CartItemController(CartItemService cartItemService, UserService userService, CartService cartService, ProductService productService) {

        this.cartItemService = cartItemService;

        this.userService = userService;

        this.cartService = cartService;

        this.productService = productService;

    }

    @DeleteMapping("/{cartItemId}/delete")
    public ResponseEntity<String> deleteCartItem(@PathVariable("cartItemId") Long cartItemId, @RequestHeader("Authorization") String jwt) throws UserNotFoundException, CartItemNotFoundException {

        User user = userService.findUserProfileByJwt(jwt);

        cartItemService.deleteCartItem(user.getId(),cartItemId);

        return new ResponseEntity<>("Deleted", HttpStatus.OK);
    }

    @PutMapping("/{cartItemId}/update")
    public ResponseEntity<CartItem> updateCartItem(@PathVariable("cartItemId") Long cartItemId, @RequestHeader("Authorization") String jwt,@RequestBody CartItem cartItem) throws UserNotFoundException, CartItemNotFoundException {

        User user = userService.findUserProfileByJwt(jwt);

        CartItem cartItem1 = cartItemService.updateCartItem(user.getId(),cartItemId,cartItem);

        return new ResponseEntity<>(cartItem1, HttpStatus.CREATED);
    }

    @GetMapping("/exist")
    public ResponseEntity<CartItem> isCartItemExist(@RequestBody CartItemRequest cartItemRequest,@RequestHeader("Authorization") String jwt) throws UserNotFoundException, ProductNotFoundException {

        User user = userService.findUserProfileByJwt(jwt);

        Cart cart = cartService.findUserCart(user.getId());

        Product product = productService.findProductById(cartItemRequest.getProductId());

        CartItem cartItem = cartItemService.isCartItemExist(cart,product, cartItemRequest.getSize(), user.getId());

        return new ResponseEntity<>(cartItem, HttpStatus.OK);
    }

    @GetMapping("/{Id}")
    public ResponseEntity<CartItem> getCartItem(@PathVariable("Id") Long cartItemId) throws CartItemNotFoundException {

        CartItem cartItem = cartItemService.findCartItemById(cartItemId);

        return new ResponseEntity<>(cartItem, HttpStatus.OK);
    }
}
