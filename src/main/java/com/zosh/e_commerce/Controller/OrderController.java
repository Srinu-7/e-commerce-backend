package com.zosh.e_commerce.Controller;

import com.zosh.e_commerce.Exception.OrderNotFoundException;
import com.zosh.e_commerce.Exception.UserNotFoundException;
import com.zosh.e_commerce.Model.Address;
import com.zosh.e_commerce.Model.Orders;
import com.zosh.e_commerce.Model.User;
import com.zosh.e_commerce.ServiceInterface.OrderService;
import com.zosh.e_commerce.ServiceInterface.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    private final UserService userService;

    public OrderController(OrderService orderService, UserService userService) {

        this.orderService = orderService;

        this.userService = userService;

    }

    @PostMapping("/")
    public ResponseEntity<Orders> createOrder(@RequestBody Address address, @RequestHeader("Authorization") String jwt) throws UserNotFoundException{

        User user = userService.findUserProfileByJwt(jwt);

        Orders order = orderService.createOrder(user, address);

        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }

    @GetMapping("/user")
    public ResponseEntity<List<Orders>> usersOrders(@RequestHeader("Authorization") String jwt) throws UserNotFoundException{

        User user = userService.findUserProfileByJwt(jwt);

        List<Orders> orders = orderService.totalOrdersByCustomerId(user.getId());

        return new ResponseEntity<>(orders, HttpStatus.OK);

    }

    @GetMapping("/{Id}")
    public ResponseEntity<Orders> findOrderById(@RequestHeader("Authorization") String jwt,@PathVariable("Id") Long orderId) throws UserNotFoundException, OrderNotFoundException {

        User user = userService.findUserProfileByJwt(jwt);

        Orders order = orderService.findOrderById(orderId);

        return new ResponseEntity<>(order, HttpStatus.OK);
    }
}
