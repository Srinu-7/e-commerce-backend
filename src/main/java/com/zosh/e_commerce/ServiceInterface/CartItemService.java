package com.zosh.e_commerce.ServiceInterface;

import com.zosh.e_commerce.Exception.CartItemNotFoundException;
import com.zosh.e_commerce.Exception.UserNotFoundException;
import com.zosh.e_commerce.Model.Cart;
import com.zosh.e_commerce.Model.CartItem;
import com.zosh.e_commerce.Model.Product;
import org.springframework.stereotype.Service;

@Service
public interface CartItemService {

    public CartItem createCartItem(CartItem cartItem);

    public CartItem updateCartItem(Long userId,Long cartItemId,CartItem cartItem) throws CartItemNotFoundException, UserNotFoundException;

    public CartItem isCartItemExist(Cart cart, Product product,String size,Long userId);

    public void deleteCartItem(Long userId,Long cartItemId) throws CartItemNotFoundException,UserNotFoundException;

    public CartItem findCartItemById(Long cartItemId) throws CartItemNotFoundException;

}
