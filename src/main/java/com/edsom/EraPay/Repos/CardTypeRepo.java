package com.edsom.EraPay.Repos;

import com.edsom.EraPay.Entities.CardType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardTypeRepo extends JpaRepository<CardType, Long> {
}
