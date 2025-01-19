package com.zosh.e_commerce.Repository;

import com.zosh.e_commerce.Model.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Orders, Long> {

    @Query(value = "SELECT * FROM orders WHERE user_id = :userId AND order_status IN ('PLACED', 'CONFIRMED', 'SHIPPED', 'DELIVERED')", nativeQuery = true)
    List<Orders> getUserOrders(@Param("userId") Long userId);

}
