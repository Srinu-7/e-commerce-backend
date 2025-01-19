package com.zosh.e_commerce.Controller;

import com.zosh.e_commerce.Exception.CartItemNotFoundException;
import com.zosh.e_commerce.Exception.ProductNotFoundException;
import com.zosh.e_commerce.Exception.UserNotFoundException;
import com.zosh.e_commerce.Model.Cart;
import com.zosh.e_commerce.Model.User;
import com.zosh.e_commerce.Request.CartItemRequest;
import com.zosh.e_commerce.ServiceInterface.CartService;
import com.zosh.e_commerce.ServiceInterface.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    private final UserService userService;

    public CartController(CartService cartService, UserService userService) {

        this.cartService = cartService;

        this.userService = userService;

    }



    @GetMapping("/")
    public ResponseEntity<Cart> findUserCart(@RequestHeader("Authorization") String jwt) throws UserNotFoundException {

        User user = userService.findUserProfileByJwt(jwt);

        Cart cart = cartService.findUserCart(user.getId());

        return new ResponseEntity<>(cart, HttpStatus.OK);
    }

    @PutMapping("/add")
    public ResponseEntity<String> addItemToCart(@RequestHeader("Authorization") String jwt, @RequestBody CartItemRequest cartItemRequest) throws UserNotFoundException, ProductNotFoundException, CartItemNotFoundException {

        User user = userService.findUserProfileByJwt(jwt);

        String response = cartService.addItemToCart(user.getId(), cartItemRequest);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
