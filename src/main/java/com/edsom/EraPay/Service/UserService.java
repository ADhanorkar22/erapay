package com.edsom.EraPay.Service;

import com.edsom.EraPay.Dtos.FundTransferDto;
import com.edsom.EraPay.Dtos.UserRegDto;
import org.springframework.http.ResponseEntity;

public interface UserService {

    ResponseEntity<?> register(UserRegDto dto);
    ResponseEntity<?> checkEmail(String email);
    ResponseEntity<?> availableEmail(String email);
    ResponseEntity<?> availableMobile(String mobile);
    ResponseEntity<?> availablePan(String pan);
    ResponseEntity<?> availableAdhaar(String adhaar);
    ResponseEntity<?> fundTransfer(String userid, FundTransferDto dto);
    ResponseEntity<?> sendOtpFundTransfer(String userid);
}
