package com.edsom.EraPay.Controller;

import com.edsom.EraPay.Dtos.UserRegDto;
import com.edsom.EraPay.Enums.CardStatus;
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
    public ResponseEntity<?> userList(@RequestHeader(value = "currPage") Integer currPage, @RequestHeader(value = "pageSize") Integer pageSize, @RequestHeader(value = "searchBy", required = false) String searchBy) {
        return adminService.userList(currPage, pageSize,searchBy);
    }

    @GetMapping("/allusersbalance")
    public ResponseEntity<?> allUsersBalance(@RequestHeader(value = "userid") String userid) {
        return adminService.allUsersBalance(userid);
    }

    @GetMapping("/allcardapply")
    public ResponseEntity<?> allCardApplication(@RequestHeader(value = "currPage") Integer currPage, @RequestHeader(value = "pageSize") Integer pageSize) {
        return adminService.allCardApplications(currPage, pageSize);
    }

    @PutMapping("/changeapplicationstatus")
    public ResponseEntity<?> changeApplicationStatus(@RequestHeader(value = "status") CardStatus status, @RequestHeader(value = "userid") String userId) {
        return adminService.changeApplicationStatus(status, userId);
    }

    @GetMapping("/allpayin")
    public ResponseEntity<?> allPayinReport(@RequestHeader(value = "currPage") Integer currPage, @RequestHeader(value = "pageSize") Integer pageSize) {
        return adminService.allPayinReport(currPage, pageSize);
    }

    @GetMapping("/allcards")
    public ResponseEntity<?> allCards(@RequestHeader(value = "currPage") Integer currPage, @RequestHeader(value = "pageSize") Integer pageSize) {
        return adminService.allCards(currPage, pageSize);
    }

    @PostMapping("/updateuserwallet")
    public ResponseEntity<?> updateUser(@RequestHeader(value = "userid") String userid, @RequestHeader(value = "amount") Double amount) {
        return  adminService.updateUserWallet(userid,amount);
    }


}
