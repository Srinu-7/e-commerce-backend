package com.zosh.e_commerce.Model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String title;

    String description;

    int price;

    int discountedPrice;

    int discountPercentage;

    int quantity;

    String brand;

    String color;

    @ElementCollection
    @Embedded
    List<Size> size = new ArrayList<>();

    String imageUrl;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    List<Rating> ratings = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    List<Review> reviews = new ArrayList<>();

    int numRatings;

    @JoinColumn
    @ManyToOne
    Category category;

    LocalDateTime createdAt;
}