package com.zosh.e_commerce.Repository;

import com.zosh.e_commerce.Model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    Category findByName(String name);

    @Query(value = "select c from Category c where c.name = :name and c.parentCategory = :parent")
    Category findByNameAndParent(@Param("name") String name, @Param("parent") Category parent );
}
