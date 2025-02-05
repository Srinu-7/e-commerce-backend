package com.zosh.e_commerce.Controller;

import com.zosh.e_commerce.Exception.CartItemNotFoundException;
import com.zosh.e_commerce.Exception.ProductNotFoundException;
import com.zosh.e_commerce.Exception.UserNotFoundException;
import com.zosh.e_commerce.Model.Cart;
import com.zosh.e_commerce.Model.CartItem;
import com.zosh.e_commerce.Model.Product;
import com.zosh.e_commerce.Model.User;
import com.zosh.e_commerce.Request.CartItemRequest;
import com.zosh.e_commerce.ServiceInterface.CartService;
import com.zosh.e_commerce.ServiceInterface.ProductService;
import com.zosh.e_commerce.ServiceInterface.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    private final UserService userService;

    private final ProductService productService;

    public CartController(CartService cartService, UserService userService,ProductService productService) {

        this.cartService = cartService;

        this.userService = userService;

        this.productService = productService;

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

    @DeleteMapping("/{cartItemId}/delete")
    public ResponseEntity<CartItem> deleteCartItem(@PathVariable("cartItemId") Long cartItemId, @RequestHeader("Authorization") String jwt) throws UserNotFoundException, CartItemNotFoundException {

        User user = userService.findUserProfileByJwt(jwt);

        CartItem cartItem = cartService.deleteCartItem(user.getId(),cartItemId);

        return new ResponseEntity<>(cartItem, HttpStatus.OK);
    }


    @GetMapping("/exist")
    public ResponseEntity<CartItem> isCartItemExist(@RequestBody CartItemRequest cartItemRequest,@RequestHeader("Authorization") String jwt) throws UserNotFoundException, ProductNotFoundException {

        User user = userService.findUserProfileByJwt(jwt);

        Cart cart = cartService.findUserCart(user.getId());

        Product product = productService.findProductById(cartItemRequest.getProductId());

        CartItem cartItem = cartService.isCartItemExist(cart,product, cartItemRequest.getSize(), user.getId());

        return new ResponseEntity<>(cartItem, HttpStatus.OK);
    }

    @GetMapping("/{Id}")
    public ResponseEntity<CartItem> getCartItem(@PathVariable("Id") Long cartItemId) throws CartItemNotFoundException {

        CartItem cartItem = cartService.findCartItemByCartItemId(cartItemId);

        return new ResponseEntity<>(cartItem, HttpStatus.OK);
    }

    @PutMapping("/update/{cartItemId}/{quantity}")
    public ResponseEntity<CartItem> updateCartItem(@PathVariable("cartItemId") Long cartItemId, @PathVariable("quantity") int quantity,@RequestHeader("Authorization") String jwt) throws CartItemNotFoundException,UserNotFoundException{

         User user = userService.findUserProfileByJwt(jwt);

        CartItem cartItem = cartService.updateCartItem(cartItemId,quantity,user.getId());

        return new ResponseEntity<>(cartItem, HttpStatus.OK);

    }

}
