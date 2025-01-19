package com.zosh.e_commerce.ServiceInterface;

import com.zosh.e_commerce.Exception.OrderNotFoundException;
import com.zosh.e_commerce.Model.Address;
import com.zosh.e_commerce.Model.Orders;
import com.zosh.e_commerce.Model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OrderService {

    public Orders createOrder(User user, Address address);

    public Orders findOrderById(Long id) throws OrderNotFoundException;

    public List<Orders> totalOrdersByCustomerId(Long userId);

    public Orders placedOrder(Long orderId) throws OrderNotFoundException;

    public Orders confirmedOrder(Long orderId) throws OrderNotFoundException;

    public Orders shippedOrder(Long orderId) throws OrderNotFoundException;

    public Orders deliveredOrder(Long orderId) throws OrderNotFoundException;

    public Orders canceledOrder(Long orderId) throws OrderNotFoundException;

    public List<Orders> getAllOrders();

    public void deleteOrderById(Long orderId) throws OrderNotFoundException;
}
