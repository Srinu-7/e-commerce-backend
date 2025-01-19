package com.zosh.e_commerce.Repository;

import com.zosh.e_commerce.Model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
