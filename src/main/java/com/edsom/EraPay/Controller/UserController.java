package com.edsom.EraPay.Controller;

import com.edsom.EraPay.Dtos.FundTransferDto;
import com.edsom.EraPay.Dtos.UserRegDto;
import com.edsom.EraPay.Service.EmailServiceImpl;
import com.edsom.EraPay.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class UserController {




    @Autowired
    private EmailServiceImpl emailService;



    @Autowired
    UserService userService;

    @GetMapping("/hello")
    public String systemCheck() {
        return "Yess Hello..!!";
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserRegDto dto) {
        return userService.register(dto);
    }

    @GetMapping("/checkpassword")
    public ResponseEntity<?> checkPassword(@RequestHeader String email) {
        return userService.checkEmail(email);
    }

    @GetMapping("/checkemail")
    public ResponseEntity<?> checkEmail(@RequestHeader String email) {
        return userService.availableEmail(email);
    }

    @GetMapping("/checkmobile")
    public ResponseEntity<?> checkMobile(@RequestHeader String mobile) {
        return userService.availableMobile(mobile);
    }

    @GetMapping("/checkadhaar")
    public ResponseEntity<?> checkAdhaar(@RequestHeader String adhaar) {
        return userService.availableAdhaar(adhaar);
    }

    @GetMapping("/checkpan")
    public ResponseEntity<?> checkPan(@RequestHeader String pan) {
        return userService.availablePan(pan);
    }

    @GetMapping("/fundtransfer")
    public ResponseEntity<?> checkPan(@RequestHeader String userid, FundTransferDto dto) {
        return userService.fundTransfer(userid, dto);
    }

    @GetMapping("/contactUs")
    public void contactUs() {

        emailService.sendWelcomeEmail();
    }


    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody Map<String,String> data) {
        System.out.println(data);
        userService.resetPassword(data.get("token"), data.get("newPassword"));
        return ResponseEntity.ok("Password successfully reset.");
    }

}
