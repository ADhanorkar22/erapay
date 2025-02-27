package com.edsom.EraPay.Service;

import org.springframework.http.ResponseEntity;

public interface Admin {
    public ResponseEntity<?> userList(String userid, Integer currPage, Integer pageSize);
    public ResponseEntity<?> allUsersBalance(String userid);
}
