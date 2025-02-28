package com.edsom.EraPay.Service;

import com.edsom.EraPay.Dtos.UserRegDto;
import com.edsom.EraPay.Enums.CardStatus;
import org.springframework.http.ResponseEntity;

public interface Admin {
    public ResponseEntity<?> userList(String userid, Integer currPage, Integer pageSize);
    public ResponseEntity<?> allUsersBalance(String userid);
    ResponseEntity<?> allCardApplications(Integer currPage, Integer pageSize);
    ResponseEntity<?> changeApplicationStatus(CardStatus status, String userId);
    ResponseEntity<?> register(UserRegDto dto);
    ResponseEntity<?> allPayinReport(Integer currPage, Integer pageSize);
    ResponseEntity<?> allCards(Integer currPage, Integer pageSize);
    ResponseEntity<?> updateUserWallet(String userid);
}
