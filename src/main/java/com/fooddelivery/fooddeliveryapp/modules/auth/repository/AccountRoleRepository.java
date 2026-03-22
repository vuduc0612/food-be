package com.fooddelivery.fooddeliveryapp.modules.auth.repository;

import com.fooddelivery.fooddeliveryapp.constant.RoleType;
import com.fooddelivery.fooddeliveryapp.modules.auth.entity.Account;
import com.fooddelivery.fooddeliveryapp.modules.auth.entity.AccountRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRoleRepository extends JpaRepository<AccountRole, Long> {
    boolean existsByAccountAndRoleType(Account account, RoleType roleType);
}
