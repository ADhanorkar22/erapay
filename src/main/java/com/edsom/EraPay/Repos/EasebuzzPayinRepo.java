package com.edsom.EraPay.Repos;

import com.edsom.EraPay.Entities.EasebuzzPayin;
import com.edsom.EraPay.Entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EasebuzzPayinRepo extends JpaRepository<EasebuzzPayin, String> {
    Page<EasebuzzPayin> findByUser(User u, Pageable pageRequest);
    EasebuzzPayin findByTxnid(String txnid);
}
