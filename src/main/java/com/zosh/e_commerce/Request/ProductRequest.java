package com.zosh.e_commerce.Request;

import com.zosh.e_commerce.Model.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductRequest {

    String title;

    String description;

    int price;

    int quantity;

    int discountedPrice;

    int discountPersent;

    String brand;

    String color;

    List<Size> size = new ArrayList<>();

    String imageUrl;

    String topLavelCategory;

    String secondLavelCategory;

    String thirdLavelCategory;

}
