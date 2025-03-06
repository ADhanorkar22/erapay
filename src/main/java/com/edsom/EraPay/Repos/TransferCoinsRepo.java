package com.edsom.EraPay.Repos;

import com.edsom.EraPay.Entities.TransferCoins;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransferCoinsRepo extends JpaRepository<TransferCoins, String> {
}
