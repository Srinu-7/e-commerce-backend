package com.zosh.e_commerce.ServiceImplementation;

import com.zosh.e_commerce.Model.OrderItem;
import com.zosh.e_commerce.Repository.OrderItemRepository;
import com.zosh.e_commerce.ServiceInterface.OrderItemService;
import org.springframework.stereotype.Service;

@Service
public class OrderItemServiceImplementation implements OrderItemService {

    private  final OrderItemRepository orderItemRepository;

    public OrderItemServiceImplementation(OrderItemRepository orderItemRepository) {

        this.orderItemRepository = orderItemRepository;

    }

    @Override
    public OrderItem createOrderItem(OrderItem orderItem) {

        return orderItemRepository.save(orderItem);

    }
}
