package com.zosh.e_commerce.Repository;

import com.zosh.e_commerce.Model.Cart;
import com.zosh.e_commerce.Model.CartItem;
import com.zosh.e_commerce.Model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    @Query(value = "select * from cart_item where user_id = :userId and product_id = :productId and cart_id = :cartId and size = :size",nativeQuery = true)
    CartItem findCartItem(@Param("cartId") Long cartId, @Param("productId") Long productId,@Param("size")String size,@Param("userId")Long userId);
}
