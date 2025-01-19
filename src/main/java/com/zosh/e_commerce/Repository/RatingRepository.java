package com.zosh.e_commerce.Repository;

import com.zosh.e_commerce.Model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {

    @Query(value = "select * from rating where  product_id = :productId",nativeQuery = true)
    List<Rating> findByProductId(@Param("productId") Long productId);

}
