package com.edsom.EraPay.Repos;

import com.edsom.EraPay.Entities.CardApply;
import com.edsom.EraPay.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardApplyRepo extends JpaRepository<CardApply, Long> {
    CardApply findByUser(User user);
}
