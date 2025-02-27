package com.edsom.EraPay.Repos;

import com.edsom.EraPay.Entities.User;
import com.edsom.EraPay.Enums.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User, String> {

    User findByEmail(String email);
    User findByMobile(String mobile);
    User findByAdhaar(String adhaar);
    User findByPan(String pan);
    User findByEmailOrMobileOrAdhaarOrPan(String email, String mobile, String adhaar, String pan);
    @Query("SELECT COALESCE(SUM(u.wallet), 0) FROM User u")
    Double getTotalWalletBalance();
}
