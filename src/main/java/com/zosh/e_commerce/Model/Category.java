package com.zosh.e_commerce.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Optional: Use this if you want auto-generated IDs
    Long id;

    @Size(min = 2, max = 50)
    @Column(nullable = false)
    String name;

    @ManyToOne
    @JoinColumn(name = "parentCategoryId")
    Category parentCategory;

    int level;
}
