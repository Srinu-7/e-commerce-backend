package com.zosh.e_commerce.Repository;

import com.zosh.e_commerce.Model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query(value = "SELECT p FROM Product p WHERE "
            + "(:category = 'null' OR p.category.name = :category) "
            + "AND (:minPrice IS NULL OR p.discountedPrice >= :minPrice) "
            + "AND (:maxPrice IS NULL OR p.discountedPrice <= :maxPrice) "
            + "AND (:minDiscount IS NULL OR p.discountPercentage >= :minDiscount) "
            + "ORDER BY CASE WHEN :sort = 'low-high' THEN p.discountedPrice "
            + "              WHEN :sort = 'high-low' THEN p.discountedPrice "
            + "              ELSE p.discountedPrice END")
    List<Product> filterProducts(@Param("category") String category,
                                 @Param("minPrice") Integer minPrice,
                                 @Param("maxPrice") Integer maxPrice,
                                 @Param("minDiscount") Integer minDiscount,
                                 @Param("sort") String sort);


    @Query(value = "select p from Product p where p.category.name = :category")
    List<Product> findByCategory(@Param("category") String category);

}
