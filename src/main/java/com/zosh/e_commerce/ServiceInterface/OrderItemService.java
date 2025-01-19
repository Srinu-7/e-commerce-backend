package com.zosh.e_commerce.ServiceInterface;

import com.zosh.e_commerce.Model.OrderItem;
import org.springframework.stereotype.Service;

@Service
public interface OrderItemService {
    public OrderItem createOrderItem(OrderItem orderItem);
}
