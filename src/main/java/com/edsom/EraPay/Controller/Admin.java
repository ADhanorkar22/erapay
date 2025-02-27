package com.edsom.EraPay.Controller;

import com.edsom.EraPay.Dtos.UserRegDto;
import com.edsom.EraPay.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class Admin {

    @Autowired
    com.edsom.EraPay.Service.Admin adminService;

    @Autowired
    UserService userService;

    @GetMapping("/users")
    public ResponseEntity<?> userList(@RequestHeader(value = "userid") String userid, @RequestHeader(value = "currPage") Integer currPage, @RequestHeader(value = "pageSize") Integer pageSize) {
        return adminService.userList(userid, currPage, pageSize);
    }

    @GetMapping("/allusersbalance")
    public ResponseEntity<?> allUsersBalance(@RequestHeader(value = "userid") String userid) {
        return adminService.allUsersBalance(userid);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserRegDto dto) {
        return userService.register(dto);
    }

    @GetMapping("/allcardapply")
    public ResponseEntity<?> allCardApplication(@RequestHeader(value = "currPage") Integer currPage, @RequestHeader(value = "pageSize") Integer pageSize){
        return adminService.allCardApplications(currPage,pageSize);
    }
}
