package com.food_delivery_app.food_delivery_back_end.modules.user.repository;

import com.food_delivery_app.food_delivery_back_end.modules.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}
