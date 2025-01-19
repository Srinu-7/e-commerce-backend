package com.zosh.e_commerce.ServiceImplementation;

import com.zosh.e_commerce.Exception.OrderNotFoundException;
import com.zosh.e_commerce.Model.*;
import com.zosh.e_commerce.Repository.AddressRepository;
import com.zosh.e_commerce.Repository.OrderItemRepository;
import com.zosh.e_commerce.Repository.OrderRepository;
import com.zosh.e_commerce.Repository.UserRepository;
import com.zosh.e_commerce.ServiceInterface.CartService;
import com.zosh.e_commerce.ServiceInterface.OrderItemService;
import com.zosh.e_commerce.ServiceInterface.OrderService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class OrderServiceImplementation implements OrderService {

    private final OrderRepository orderRepository;

    private final CartService cartService;

    private final AddressRepository addressRepository;

    private final UserRepository userRepository;

    private final OrderItemRepository orderItemRepository;

    private final OrderItemService orderItemService;

    public OrderServiceImplementation(OrderRepository orderRepository, CartService cartService,AddressRepository addressRepository, UserRepository userRepository, OrderItemRepository orderItemRepository, OrderItemService orderItemService) {

        this.orderRepository = orderRepository;

        this.cartService = cartService;

        this.addressRepository = addressRepository;

        this.userRepository = userRepository;

        this.orderItemRepository = orderItemRepository;

        this.orderItemService = orderItemService;

    }

    @Override
    public Orders createOrder(User user, Address address) {

        address.setUser(user);

        address = addressRepository.save(address);

        user.getAddressList().add(address);

        userRepository.save(user);

        Cart cart = cartService.findUserCart(user.getId());

        List<OrderItem> orderItems = new ArrayList<>();

        Set<CartItem> items = new HashSet<>(cart.getItems());

        for (CartItem item : items) {

            OrderItem orderItem = new OrderItem();

            orderItem.setPrice(item.getPrice());

            orderItem.setQuantity(item.getQuantity());

            orderItem.setProduct(item.getProduct());

            orderItem.setSize(item.getSize());

            orderItem.setUserId(item.getUserId());

            orderItem.setDiscountedPrice(item.getDiscountedPrice());

            orderItem = orderItemRepository.save(orderItem);

            orderItems.add(orderItem);
        }

        Orders order = new Orders();

        order.setUser(user);

        order.setOrderItems(orderItems);

        order.setTotalPrice(cart.getTotalPrice());

        order.setTotalDiscountPrice(cart.getTotalDiscountedPrice());

        order.setTotalDiscount(cart.getDiscount());

        order.setTotalQuantity(cart.getTotalQuantity());

        order.setShippingAddress(address);

        order.setOrderDate(LocalDateTime.now());

        order.setOrderStatus("PENDING");

        order.getPaymentDetails().setStatus("PENDING");

        order.setCreatedAt(LocalDateTime.now());

        order = orderRepository.save(order);

        for(OrderItem orderItem : orderItems) {

            orderItem.setOrders(order);

            orderItemRepository.save(orderItem);
        }

        return order;
    }

    @Override
    public Orders findOrderById(Long id) throws OrderNotFoundException{

        Orders order = orderRepository.findById(id).get();

        if(order == null) throw new OrderNotFoundException("Order not found with id " + id);

        return order;
    }

    @Override
    public List<Orders> totalOrdersByCustomerId(Long customerId) {

        return orderRepository.getUserOrders(customerId);

    }

    @Override
    public Orders placedOrder(Long orderId) throws OrderNotFoundException {

        Orders order = findOrderById(orderId);

        order.setOrderStatus("PLACED");

        order.getPaymentDetails().setStatus("COMPLETED");

        return orderRepository.save(order);
    }

    @Override
    public Orders confirmedOrder(Long orderId) throws OrderNotFoundException {

        Orders order = findOrderById(orderId);

        order.setOrderStatus("CONFIRMED");

        return orderRepository.save(order);
    }

    @Override
    public Orders shippedOrder(Long orderId) throws OrderNotFoundException {

        Orders order = findOrderById(orderId);

        order.setOrderStatus("SHIPPED");

        return orderRepository.save(order);
    }

    @Override
    public Orders deliveredOrder(Long orderId) throws OrderNotFoundException {

        Orders order = findOrderById(orderId);

        order.setOrderStatus("DELIVERED");

        order.setDeliveryDate(LocalDateTime.now());

        return orderRepository.save(order);
    }

    @Override
    public Orders canceledOrder(Long orderId) throws OrderNotFoundException {

        Orders order = findOrderById(orderId);

        order.setOrderStatus("CANCELLED");

        return orderRepository.save(order);
    }

    @Override
    public List<Orders> getAllOrders() {

        return orderRepository.findAll();
    }

    @Override
    public void deleteOrderById(Long orderId) throws OrderNotFoundException {

        Orders order = findOrderById(orderId);

        orderRepository.deleteById(orderId);
    }
}
