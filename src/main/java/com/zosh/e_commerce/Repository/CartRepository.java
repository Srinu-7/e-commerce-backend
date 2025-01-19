package com.zosh.e_commerce.Repository;

import com.zosh.e_commerce.Model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart,Long> {

    @Query(value = "select c from Cart c where c.user.id = :userId")
    Cart findByUserId(@Param("userId") Long userId);
}
