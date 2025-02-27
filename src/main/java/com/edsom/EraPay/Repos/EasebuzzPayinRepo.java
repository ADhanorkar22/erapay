package com.edsom.EraPay.Repos;

import com.edsom.EraPay.Entities.EasebuzzPayin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EasebuzzPayinRepo extends JpaRepository<EasebuzzPayin, String> {
}
