package com.zosh.e_commerce.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Optional: Use this if you want auto-generated IDs
    Long id;

    String firstName;

    String lastName;

    String email;

    String password;

    String phone;

    String role;

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    List<Address> addressList = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "paymentInformation",joinColumns = @JoinColumn)
    List<PaymentInformation> paymentInformation = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    List<Rating> ratingList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    List<Review> reviewList = new ArrayList<>();

    LocalDateTime createdAt;

}
