package com.edsom.EraPay.Controller;

import com.edsom.EraPay.Dtos.FundTransferDto;
import com.edsom.EraPay.Dtos.UserRegDto;
import com.edsom.EraPay.EasbuzzUtil.PaymentRequest;
import com.edsom.EraPay.Service.EaseBuzzPayInService;
import com.edsom.EraPay.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    EaseBuzzPayInService easeBuzzPayInService;

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

    @GetMapping("/payin")
    public ResponseEntity<?> payIn(@RequestBody PaymentRequest req){
        return easeBuzzPayInService.initiatePayment(req);
    }
}
