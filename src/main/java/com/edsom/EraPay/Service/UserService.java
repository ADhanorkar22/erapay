package com.edsom.EraPay.Service;

import com.edsom.EraPay.Dtos.FundTransferDto;
import com.edsom.EraPay.Dtos.UserRegDto;
import com.edsom.EraPay.Dtos.UserUpdateDto;
import org.springframework.http.ResponseEntity;

public interface UserService {


    ResponseEntity<?> checkEmail(String email);
    ResponseEntity<?> availableEmail(String email);
    ResponseEntity<?> availableMobile(String mobile);
    ResponseEntity<?> availablePan(String pan);
    ResponseEntity<?> availableAdhaar(String adhaar);
    ResponseEntity<?> fundTransfer(String userid, FundTransferDto dto);
    ResponseEntity<?> sendOtpFundTransfer(String userid);

    void resetPassword(String token, String newPassword);
    ResponseEntity<?> cardApply(String userid, String cardtype);
    ResponseEntity<?> payinReports(String userid, Integer currPage, Integer pageSize);
    ResponseEntity<?> updateUser(UserUpdateDto dto);
    ResponseEntity<?> fetchBalance(String userid);
    ResponseEntity<?> myInfo(String userid);
}
