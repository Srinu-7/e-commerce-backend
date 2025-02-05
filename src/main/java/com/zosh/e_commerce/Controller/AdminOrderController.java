package com.zosh.e_commerce.Controller;

import com.zosh.e_commerce.Exception.OrderNotFoundException;
import com.zosh.e_commerce.Model.Orders;
import com.zosh.e_commerce.ServiceInterface.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/orders")
public class AdminOrderController {

    private final OrderService orderService;

    public AdminOrderController(OrderService orderService) {

        this.orderService = orderService;

    }

    @GetMapping("/")
    public ResponseEntity<List<Orders>> getAllOrdersHandler(){

        List<Orders> orders = orderService.getAllOrders();

        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @PutMapping("/{orderId}/confirmed")
    public ResponseEntity<Orders> ConfirmedOrderHandler(@PathVariable Long orderId, @RequestHeader("Authorization") String jwt) throws OrderNotFoundException {

        Orders order = orderService.confirmedOrder(orderId);

        return new ResponseEntity<>(order, HttpStatus.OK);

     }

    @PutMapping("/{orderId}/shipped")
    public ResponseEntity<Orders> ShippedOrderHandler(@PathVariable Long orderId, @RequestHeader("Authorization") String jwt) throws OrderNotFoundException {

        Orders order = orderService.shippedOrder(orderId);

        return new ResponseEntity<>(order, HttpStatus.OK);

    }

    @PutMapping("/{orderId}/delivered")
    public ResponseEntity<Orders> DeliveredOrderHandler(@PathVariable Long orderId, @RequestHeader("Authorization") String jwt) throws OrderNotFoundException {

        Orders order = orderService.deliveredOrder(orderId);

        return new ResponseEntity<>(order, HttpStatus.OK);

    }

    @PutMapping("/{orderId}/cancelled")
    public ResponseEntity<Orders> CancelledOrderHandler(@PathVariable Long orderId, @RequestHeader("Authorization") String jwt) throws OrderNotFoundException {

        Orders order = orderService.canceledOrder(orderId);

        return new ResponseEntity<>(order, HttpStatus.OK);

    }

    @DeleteMapping("/{orderId}/deleted")
    public String DeletedOrderHandler(@PathVariable Long orderId, @RequestHeader("Authorization") String jwt) throws OrderNotFoundException {

        orderService.deleteOrderById(orderId);

        return "Deleted Order Successfully";

    }
}
