package com.zosh.e_commerce.Model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String orderId;

    @ManyToOne
    User user;

    @OneToMany(mappedBy = "orders", cascade = CascadeType.ALL)
    List<OrderItem> orderItems = new ArrayList<>();

    LocalDateTime orderDate;

    LocalDateTime deliveryDate;

    @OneToOne
    Address shippingAddress;

    @Embedded
    PaymentDetails paymentDetails;

    double totalPrice;

    Integer totalDiscountPrice;

    Integer totalDiscount;

    String orderStatus;

    Integer totalQuantity;

    LocalDateTime createdAt;
}
