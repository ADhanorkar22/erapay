package com.edsom.EraPay.Repos;

import com.edsom.EraPay.Entities.Card;
import com.edsom.EraPay.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardRepo extends JpaRepository<Card, Long> {
    Card findByUser(User user);
}
