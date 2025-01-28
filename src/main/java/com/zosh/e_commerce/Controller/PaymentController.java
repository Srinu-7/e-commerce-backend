package com.zosh.e_commerce.Controller;

import com.razorpay.Payment;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.zosh.e_commerce.Exception.OrderNotFoundException;
import com.zosh.e_commerce.Model.Orders;
import com.zosh.e_commerce.Repository.OrderRepository;
import com.zosh.e_commerce.Response.PaymentLinkResponse;
import com.zosh.e_commerce.ServiceInterface.OrderService;
import com.zosh.e_commerce.ServiceInterface.UserService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    @Value("${razorpay.api.key}")
    String apiKey;

    @Value("${razorpay.api.secret}")
    String apiSecret;

    private final OrderService orderService;
    private final UserService userService;
    private final OrderRepository orderRepository;

    public PaymentController(OrderService orderService, UserService userService, OrderRepository orderRepository) {
        this.orderService = orderService;
        this.userService = userService;
        this.orderRepository = orderRepository;
    }

    @PostMapping("/{orderId}")
    public ResponseEntity<PaymentLinkResponse> createPaymentLink(@PathVariable("orderId")Long orderId) throws OrderNotFoundException, RazorpayException {

        System.out.println("orderId  "+orderId);

        Orders order = orderRepository.findById(orderId).get();

        try{
            RazorpayClient razorpay = new RazorpayClient(apiKey,apiSecret);
            
            JSONObject paymentLinkRequest = new JSONObject();
            JSONObject customer = new JSONObject();
            JSONObject notify = new JSONObject();

            customer.put("name",order.getUser().getFirstName()+" "+order.getUser().getLastName());
            customer.put("email",order.getUser().getEmail());

            notify.put("sms",true);
            notify.put("email",true);

            paymentLinkRequest.put("amount",order.getTotalPrice()*100);
            paymentLinkRequest.put("currency", "INR");
            paymentLinkRequest.put("customer",customer);
            paymentLinkRequest.put("notify",notify);
            paymentLinkRequest.put("callback_url","http://localhost:3000/payment/"+orderId);
            paymentLinkRequest.put("callback_method","get");

            PaymentLink payment = razorpay.paymentLink.create(paymentLinkRequest);

            String paymentLinkId = payment.get("id");
            String paymentLinkUrl = payment.get("short_url");

            PaymentLinkResponse paymentLinkResponse = new PaymentLinkResponse();
            paymentLinkResponse.setPaymentLinkId(paymentLinkId);
            paymentLinkResponse.setPaymentLinkUrl(paymentLinkUrl);

            return new ResponseEntity<>(paymentLinkResponse, HttpStatus.CREATED);
        } catch (Exception e) {
            throw new RazorpayException(e.getMessage());
        }
    }

    @GetMapping("/{paymentId}/{orderId}")
    public ResponseEntity<String> redirect(@PathVariable("paymentId") String paymentId,@PathVariable("orderId") Long orderId) throws OrderNotFoundException, RazorpayException {

        Orders order = orderService.findOrderById(orderId);
        RazorpayClient razorpay = new RazorpayClient(apiKey,apiSecret);
        System.out.println("paymentId--------"+paymentId);

        try{
            Payment payment = razorpay.payments.fetch(paymentId);

            if(payment.get("status").equals("captured")){
                order.getPaymentDetails().setPaymentId(paymentId);
                order.getPaymentDetails().setStatus("COMPLETED");
                order.setOrderStatus("PLACED");
                orderRepository.save(order);
            }
            return new ResponseEntity<>("your order placed",HttpStatus.OK);
        } catch (Exception e) {
            throw new RazorpayException(e.getMessage());
        }
    }
}
