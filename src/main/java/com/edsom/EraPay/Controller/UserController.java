package com.edsom.EraPay.Controller;

import com.edsom.EraPay.Dtos.FundTransferDto;
import com.edsom.EraPay.Dtos.UserRegDto;
import com.edsom.EraPay.Dtos.UserUpdateDto;
import com.edsom.EraPay.EasbuzzUtil.PaymentRequest;
import com.edsom.EraPay.Service.EaseBuzzPayInService;
import com.edsom.EraPay.Service.UserService;
import com.edsom.EraPay.ServiceImpl.EmailServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    EaseBuzzPayInService easeBuzzPayInService;

    @GetMapping("/checkemail")
    public ResponseEntity<?> checkEmail(@RequestHeader String email) {
        System.out.println(email);
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

    @PostMapping("/fundtransfer")
    public ResponseEntity<?> fundTransfer(@RequestHeader String userid, @RequestBody FundTransferDto dto) {
        return userService.fundTransfer(userid, dto);
    }

    @PostMapping("/payin")
    public ResponseEntity<?> payIn(@RequestBody PaymentRequest req){
        return easeBuzzPayInService.initiatePayment(req);
    }

    @PostMapping("/cardapply")
    public ResponseEntity<?> cardApply(@RequestHeader(value="cardtype") String cardtype, @RequestHeader(value="userid") String userid){
        return userService.cardApply(userid,cardtype);
    }

    @GetMapping("/payinreport")
    public ResponseEntity<?> payIn(@RequestHeader(value="userid") String userid, @RequestHeader(value = "currPage") Integer currPage, @RequestHeader(value = "pageSize") Integer pageSize){
        return userService.payinReports(userid,currPage,pageSize);
    }

    @PostMapping("/updateuser")
    public ResponseEntity<?> updateProfile(@Valid @RequestBody UserUpdateDto dto){
        return userService.updateUser(dto);
    }

    @GetMapping("/fetchbalance")
    public ResponseEntity<?> fetchBalane(@RequestHeader(value="userid") String userid){
        return userService.fetchBalance(userid);
    }

    @GetMapping("/mydetails")
    public ResponseEntity<?> fetchMyInfo(@RequestHeader(value="userid") String userid){
        return userService.myInfo(userid);
    }

    @GetMapping("/mycard")
    public ResponseEntity<?> myCard(@RequestHeader(value="userid") String userid){
        return userService.myCard(userid);
    }

}
