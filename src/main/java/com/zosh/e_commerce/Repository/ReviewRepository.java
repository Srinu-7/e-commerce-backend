package com.zosh.e_commerce.Repository;

import com.zosh.e_commerce.Model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query(value = "select * from review where product_id = :productId",nativeQuery = true)
    List<Review> findByProductId(@Param("productId") Long productId);
}
