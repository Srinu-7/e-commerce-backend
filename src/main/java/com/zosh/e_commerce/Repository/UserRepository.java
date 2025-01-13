package com.zosh.e_commerce.Repository;

import com.zosh.e_commerce.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Integer> {
    @Query(value = "select * from user where email = :email",nativeQuery = true)
    User findByEmail(String email);

    @Query(value = "select * from user where id = :id",nativeQuery = true)
    User findUserById(Long id);
}
