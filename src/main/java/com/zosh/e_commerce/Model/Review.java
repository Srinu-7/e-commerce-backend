package com.zosh.e_commerce.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Optional: Use this if you want auto-generated IDs
    Long id;

    String review;

    @ManyToOne
    @JoinColumn
    @JsonIgnore
    Product product;

    @ManyToOne
    @JoinColumn
    User user;

    LocalDateTime createdAt;
}
