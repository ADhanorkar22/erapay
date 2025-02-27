package com.edsom.EraPay.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class Admin {

    @Autowired
    com.edsom.EraPay.Service.Admin adminService;

    @GetMapping("/users")
    public ResponseEntity<?> userList(@RequestHeader(value = "userid") String userid, @RequestHeader(value = "currPage") Integer currPage, @RequestHeader(value = "pageSize") Integer pageSize) {
        return adminService.userList(userid, currPage, pageSize);
    }

    @GetMapping("/allusersbalance")
    public ResponseEntity<?> allUsersBalance(@RequestHeader(value = "userid") String userid) {
        return adminService.allUsersBalance(userid);
    }
}
