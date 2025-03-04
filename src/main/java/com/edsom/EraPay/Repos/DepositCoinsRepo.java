package com.edsom.EraPay.Repos;

import com.edsom.EraPay.Entities.DepositCoins;
import com.edsom.EraPay.Entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepositCoinsRepo extends JpaRepository<DepositCoins, String> {
    Page<DepositCoins> findByUser(User user, Pageable pageable);
}
