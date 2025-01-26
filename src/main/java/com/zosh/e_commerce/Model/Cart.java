package com.zosh.e_commerce.Model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @JoinColumn
    @OneToOne
    User user;


    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL,orphanRemoval = true,fetch = FetchType.LAZY)
    List<CartItem> cartItems = new ArrayList<>();

    double totalPrice;

    int totalQuantity;

    int totalDiscountedPrice;

    int discount;

    @Override
    public String toString() {
        return "Cart{" +
                "id=" + id +
                ", user=" + user +
                ", totalPrice=" + totalPrice +
                ", totalQuantity=" + totalQuantity +
                ", totalDiscountedPrice=" + totalDiscountedPrice +
                ", discount=" + discount +
                '}';
    }

}
