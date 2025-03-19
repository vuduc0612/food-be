package com.food_delivery_app.food_delivery_back_end.modules.user.repository;

import com.food_delivery_app.food_delivery_back_end.modules.user.dto.UserDto;
import com.food_delivery_app.food_delivery_back_end.modules.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByAccountId(Long accountId);
    @Query("SELECT new com.food_delivery_app.food_delivery_back_end.modules.user.dto.UserDto(u.id, u.account.email, u.username) FROM User u")
    List<UserDto> findAllUsersWithEmailAndUsername(Pageable pageable);
}
