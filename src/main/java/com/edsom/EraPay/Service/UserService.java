package com.edsom.EraPay.Service;

import com.edsom.EraPay.Dtos.DepositCoinsDto;
import com.edsom.EraPay.Dtos.FundTransferDto;
import com.edsom.EraPay.Dtos.UserRegDto;
import com.edsom.EraPay.Dtos.UserUpdateDto;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface UserService {


    ResponseEntity<?> checkEmail(String email);
    ResponseEntity<?> availableEmail(String email);
    ResponseEntity<?> availableMobile(String mobile);
    ResponseEntity<?> availablePan(String pan);
    ResponseEntity<?> availableAdhaar(String adhaar);
    ResponseEntity<?> fundTransfer(String userid, FundTransferDto dto);
    ResponseEntity<?> sendOtpFundTransfer(String userid);

    ResponseEntity<?> resetPassword(String token, String newPassword);
    ResponseEntity<?> cardApply(String userid, String cardtype);
    ResponseEntity<?> payinReports(String userid, Integer currPage, Integer pageSize);
    ResponseEntity<?> updateUser(UserUpdateDto dto);
    ResponseEntity<?> fetchBalance(String userid);
    ResponseEntity<?> myInfo(String userid);
    ResponseEntity<?> myCard(String userid);
    ResponseEntity<?> forgetPassword(String email);
    ResponseEntity<?>getUsersCount();
    ResponseEntity<?> depositCoins(String userid, DepositCoinsDto dto, String url);
    ResponseEntity<?> myDeposits(String userid, Integer currPage, Integer pageSize);
}
